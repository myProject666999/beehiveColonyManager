import { useState, useEffect, useCallback, useMemo } from 'react';
import { Table, Button, Modal, Form, Input, InputNumber, Select, Space, Popconfirm, message, Statistic, Row, Col, Card } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { hives, harvests } from '../api';

const gradeMap = { premium: '特级', gradeA: '一级', gradeB: '二级', gradeC: '三级' };

export default function HarvestList() {
  const [hiveList, setHiveList] = useState([]);
  const [selectedHive, setSelectedHive] = useState(null);
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form] = Form.useForm();

  const fetchHives = useCallback(async () => {
    try {
      const res = await hives.list();
      setHiveList(Array.isArray(res) ? res : res.data || []);
    } catch {
      message.error('获取蜂箱列表失败');
    }
  }, []);

  const fetchData = useCallback(async () => {
    if (!selectedHive) return;
    setLoading(true);
    try {
      const res = await harvests.getByHive(selectedHive);
      setData(Array.isArray(res) ? res : res.data || []);
    } catch {
      message.error('获取取蜜记录失败');
    }
    setLoading(false);
  }, [selectedHive]);

  useEffect(() => {
    fetchHives();
  }, [fetchHives]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const stats = useMemo(() => {
    const totalWeight = data.reduce((sum, r) => sum + (r.weight || 0), 0);
    const avgWater = data.length > 0
      ? data.reduce((sum, r) => sum + (r.waterContent || 0), 0) / data.length
      : 0;
    return { totalWeight, avgWater };
  }, [data]);

  const handleAdd = () => {
    setEditing(null);
    form.resetFields();
    setModalOpen(true);
  };

  const handleEdit = (record) => {
    setEditing(record);
    form.setFieldsValue(record);
    setModalOpen(true);
  };

  const handleDelete = async (id) => {
    try {
      await harvests.delete(id);
      message.success('删除成功');
      fetchData();
    } catch {
      message.error('删除失败');
    }
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      const payload = { ...values, hiveId: selectedHive };
      if (editing) {
        await harvests.update(editing.id, payload);
        message.success('更新成功');
      } else {
        await harvests.create(payload);
        message.success('创建成功');
      }
      setModalOpen(false);
      fetchData();
    } catch {
      message.error('操作失败');
    }
  };

  const columns = [
    { title: '取蜜日期', dataIndex: 'harvestDate', key: 'harvestDate', width: 120 },
    { title: '重量(kg)', dataIndex: 'weight', key: 'weight', width: 100 },
    { title: '含水率(%)', dataIndex: 'waterContent', key: 'waterContent', width: 110 },
    { title: '蜜源', dataIndex: 'nectarSource', key: 'nectarSource', width: 120 },
    {
      title: '等级',
      dataIndex: 'grade',
      key: 'grade',
      width: 80,
      render: (v) => gradeMap[v] || v,
    },
    {
      title: '操作',
      key: 'action',
      width: 140,
      render: (_, record) => (
        <Space>
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          <Popconfirm title="确定删除?" onConfirm={() => handleDelete(record.id)}>
            <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Row gutter={16} style={{ marginBottom: 16 }}>
        <Col span={8}>
          <Card>
            <Statistic title="总产量(kg)" value={stats.totalWeight} precision={1} valueStyle={{ color: '#faad14' }} />
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <Statistic title="平均含水率(%)" value={stats.avgWater} precision={2} valueStyle={{ color: '#1890ff' }} />
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <Statistic title="取蜜次数" value={data.length} valueStyle={{ color: '#52c41a' }} />
          </Card>
        </Col>
      </Row>

      <div style={{ marginBottom: 16, display: 'flex', gap: 16, alignItems: 'center' }}>
        <Select
          placeholder="选择蜂箱"
          style={{ width: 240 }}
          value={selectedHive}
          onChange={setSelectedHive}
          showSearch
          optionFilterProp="label"
          options={hiveList.map((h) => ({ value: h.id, label: h.code }))}
        />
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd} disabled={!selectedHive}>
          新增加蜜记录
        </Button>
      </div>

      <Table
        rowKey="id"
        columns={columns}
        dataSource={data}
        loading={loading}
      />

      <Modal
        title={editing ? '编辑取蜜记录' : '新增取蜜记录'}
        open={modalOpen}
        onOk={handleSubmit}
        onCancel={() => setModalOpen(false)}
        destroyOnHidden
      >
        <Form form={form} layout="vertical">
          <Form.Item name="harvestDate" label="取蜜日期" rules={[{ required: true, message: '请输入取蜜日期' }]}>
            <Input placeholder="YYYY-MM-DD" />
          </Form.Item>
          <Form.Item name="weight" label="重量(kg)" rules={[{ required: true, message: '请输入重量' }]}>
            <InputNumber min={0} step={0.1} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="waterContent" label="含水率(%)">
            <InputNumber min={0} max={100} step={0.1} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="nectarSource" label="蜜源">
            <Input />
          </Form.Item>
          <Form.Item name="grade" label="等级">
            <Select
              allowClear
              options={[
                { value: 'premium', label: '特级' },
                { value: 'gradeA', label: '一级' },
                { value: 'gradeB', label: '二级' },
                { value: 'gradeC', label: '三级' },
              ]}
            />
          </Form.Item>
          <Form.Item name="notes" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
