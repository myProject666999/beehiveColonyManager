import { useState, useEffect, useCallback } from 'react';
import { Table, Button, Modal, Form, Input, InputNumber, Select, Space, Popconfirm, message, Tag } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, CloseOutlined } from '@ant-design/icons';
import { hives, inspections } from '../api';

const temperMap = { gentle: '温顺', normal: '一般', aggressive: '凶暴' };

const conditionColors = {
  good: 'green',
  fair: 'orange',
  poor: 'red',
};

const overallColors = {
  strong: 'green',
  medium: 'blue',
  weak: 'orange',
};

const overallMap = { strong: '强群', medium: '中等', weak: '弱群' };

const broodMap = { good: '良好', fair: '一般', poor: '差' };

export default function InspectionList() {
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
      const list = Array.isArray(res) ? res : res.data || [];
      setHiveList(list);
      if (list.length > 0 && !selectedHive) {
        setSelectedHive(list[0].id);
      }
    } catch {
      message.error('获取蜂箱列表失败');
    }
  }, [selectedHive]);

  const fetchData = useCallback(async () => {
    if (!selectedHive) return;
    setLoading(true);
    try {
      const res = await inspections.getByHive(selectedHive);
      setData(Array.isArray(res) ? res : res.data || []);
    } catch {
      message.error('获取检查记录失败');
    }
    setLoading(false);
  }, [selectedHive]);

  useEffect(() => {
    fetchHives();
  }, [fetchHives]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

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
      await inspections.delete(id);
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
        await inspections.update(editing.id, payload);
        message.success('更新成功');
      } else {
        await inspections.create(payload);
        message.success('创建成功');
      }
      setModalOpen(false);
      fetchData();
    } catch {
      message.error('操作失败');
    }
  };

  const renderBool = (val) =>
    val ? (
      <CheckOutlined style={{ color: '#52c41a', fontSize: 16 }} />
    ) : (
      <CloseOutlined style={{ color: '#ff4d4f', fontSize: 16 }} />
    );

  const columns = [
    { title: '检查日期', dataIndex: 'inspectionDate', key: 'inspectionDate', width: 120 },
    { title: '蜂王在否', dataIndex: 'queenPresent', key: 'queenPresent', width: 100, render: renderBool },
    { title: '蜂螨', dataIndex: 'hasMites', key: 'hasMites', width: 80, render: renderBool },
    { title: '有疾病', dataIndex: 'hasDisease', key: 'hasDisease', width: 80, render: renderBool },
    { title: '病情描述', dataIndex: 'diseaseDetail', key: 'diseaseDetail', width: 140 },
    { title: '储蜜(kg)', dataIndex: 'honeyStore', key: 'honeyStore', width: 100 },
    { title: '子脾', dataIndex: 'broodCondition', key: 'broodCondition', width: 90, render: (v) => broodMap[v] || v },
    { title: '性情', dataIndex: 'temper', key: 'temper', width: 90, render: (v) => temperMap[v] || v },
    {
      title: '整体评估',
      dataIndex: 'overallCondition',
      key: 'overallCondition',
      width: 100,
      render: (v) => {
        const label = overallMap[v] || v;
        return <Tag color={overallColors[v] || 'default'}>{label}</Tag>;
      },
    },
    { title: '备注', dataIndex: 'notes', key: 'notes' },
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
      <div style={{ marginBottom: 16, display: 'flex', gap: 16, alignItems: 'center' }}>
        <Select
          placeholder="选择蜂箱"
          style={{ width: 240 }}
          value={selectedHive}
          onChange={setSelectedHive}
          showSearch
          optionFilterProp="label"
          options={hiveList.map((h) => ({ value: h.id, label: h.hiveNumber || h.code }))}
        />
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd} disabled={!selectedHive}>
          新增检查记录
        </Button>
      </div>

      <Table
        rowKey="id"
        columns={columns}
        dataSource={data}
        loading={loading}
      />

      <Modal
        title={editing ? '编辑检查记录' : '新增检查记录'}
        open={modalOpen}
        onOk={handleSubmit}
        onCancel={() => setModalOpen(false)}
        destroyOnHidden
        width={560}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="inspectionDate" label="检查日期" rules={[{ required: true, message: '请输入检查日期' }]}>
            <Input placeholder="YYYY-MM-DD" />
          </Form.Item>
          <Form.Item name="queenPresent" label="蜂王在否">
            <Select placeholder="请选择" allowClear options={[{ value: true, label: '是' }, { value: false, label: '否' }]} />
          </Form.Item>
          <Form.Item name="hasMites" label="是否有蜂螨">
            <Select placeholder="请选择" allowClear options={[{ value: true, label: '有' }, { value: false, label: '无' }]} />
          </Form.Item>
          <Form.Item name="hasDisease" label="是否有疾病">
            <Select placeholder="请选择" allowClear options={[{ value: true, label: '有' }, { value: false, label: '无' }]} />
          </Form.Item>
          <Form.Item name="diseaseDetail" label="病情描述">
            <Input />
          </Form.Item>
          <Form.Item name="honeyStore" label="储蜜量(kg)">
            <InputNumber min={0} step={0.1} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="broodCondition" label="子脾状况">
            <Select
              allowClear
              options={[
                { value: 'good', label: '良好' },
                { value: 'fair', label: '一般' },
                { value: 'poor', label: '差' },
              ]}
            />
          </Form.Item>
          <Form.Item name="temper" label="蜂群性情">
            <Select
              allowClear
              options={[
                { value: 'gentle', label: '温顺' },
                { value: 'normal', label: '一般' },
                { value: 'aggressive', label: '凶暴' },
              ]}
            />
          </Form.Item>
          <Form.Item name="overallCondition" label="整体评估">
            <Select
              allowClear
              options={[
                { value: 'strong', label: '强群' },
                { value: 'medium', label: '中等' },
                { value: 'weak', label: '弱群' },
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
