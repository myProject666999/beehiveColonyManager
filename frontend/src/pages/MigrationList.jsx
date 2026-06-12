import { useState, useEffect, useCallback } from 'react';
import { Table, Button, Modal, Form, Input, InputNumber, Select, Space, Popconfirm, message, Timeline, Card, Switch } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { apiaries, migrations } from '../api';

export default function MigrationList() {
  const [apiaryList, setApiaryList] = useState([]);
  const [selectedApiary, setSelectedApiary] = useState(null);
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form] = Form.useForm();
  const [viewMode, setViewMode] = useState('table');

  const fetchApiaries = useCallback(async () => {
    try {
      const res = await apiaries.list();
      setApiaryList(Array.isArray(res) ? res : res.data || []);
    } catch {
      message.error('获取蜂场列表失败');
    }
  }, []);

  const fetchData = useCallback(async () => {
    if (!selectedApiary) return;
    setLoading(true);
    try {
      const res = await migrations.getByApiary(selectedApiary);
      setData(Array.isArray(res) ? res : res.data || []);
    } catch {
      message.error('获取转场记录失败');
    }
    setLoading(false);
  }, [selectedApiary]);

  useEffect(() => {
    fetchApiaries();
  }, [fetchApiaries]);

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
      await migrations.delete(id);
      message.success('删除成功');
      fetchData();
    } catch {
      message.error('删除失败');
    }
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      const payload = { ...values, apiaryId: selectedApiary };
      if (editing) {
        await migrations.update(editing.id, payload);
        message.success('更新成功');
      } else {
        await migrations.create(payload);
        message.success('创建成功');
      }
      setModalOpen(false);
      fetchData();
    } catch {
      message.error('操作失败');
    }
  };

  const columns = [
    { title: '出发日期', dataIndex: 'departureDate', key: 'departureDate', width: 120 },
    { title: '出发地', dataIndex: 'departureLocation', key: 'departureLocation', width: 140 },
    { title: '目的地', dataIndex: 'destinationLocation', key: 'destinationLocation', width: 140 },
    { title: '到达日期', dataIndex: 'arrivalDate', key: 'arrivalDate', width: 120 },
    { title: '运输车辆', dataIndex: 'vehicle', key: 'vehicle', width: 120 },
    { title: '蜂箱数', dataIndex: 'hiveCount', key: 'hiveCount', width: 80 },
    { title: '费用', dataIndex: 'cost', key: 'cost', width: 100, render: (v) => v != null ? `¥${v}` : '-' },
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

  const renderTimeline = () => (
    <Card>
      <Timeline
        items={data.map((item) => ({
          color: 'blue',
          children: (
            <div key={item.id}>
              <div style={{ fontWeight: 'bold', marginBottom: 4 }}>
                {item.departureDate} - {item.arrivalDate}
              </div>
              <div>
                {item.departureLocation} → {item.destinationLocation}
              </div>
              <div style={{ color: '#888', fontSize: 13 }}>
                车辆: {item.vehicle || '-'} | 蜂箱数: {item.hiveCount || '-'} | 费用: {item.cost != null ? `¥${item.cost}` : '-'}
              </div>
              <div style={{ marginTop: 4 }}>
                <Space>
                  <Button type="link" size="small" icon={<EditOutlined />} onClick={() => handleEdit(item)}>编辑</Button>
                  <Popconfirm title="确定删除?" onConfirm={() => handleDelete(item.id)}>
                    <Button type="link" size="small" danger icon={<DeleteOutlined />}>删除</Button>
                  </Popconfirm>
                </Space>
              </div>
            </div>
          ),
        }))}
      />
      {data.length === 0 && <div style={{ color: '#999', textAlign: 'center' }}>暂无转场记录</div>}
    </Card>
  );

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', gap: 16, alignItems: 'center' }}>
        <Select
          placeholder="选择蜂场"
          style={{ width: 240 }}
          value={selectedApiary}
          onChange={setSelectedApiary}
          showSearch
          optionFilterProp="label"
          options={apiaryList.map((a) => ({ value: a.id, label: a.name }))}
        />
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd} disabled={!selectedApiary}>
          新增转场记录
        </Button>
        <div style={{ marginLeft: 'auto', display: 'flex', alignItems: 'center', gap: 8 }}>
          <span>时间线视图</span>
          <Switch checked={viewMode === 'timeline'} onChange={(v) => setViewMode(v ? 'timeline' : 'table')} />
        </div>
      </div>

      {viewMode === 'table' ? (
        <Table
          rowKey="id"
          columns={columns}
          dataSource={data}
          loading={loading}
        />
      ) : (
        renderTimeline()
      )}

      <Modal
        title={editing ? '编辑转场记录' : '新增转场记录'}
        open={modalOpen}
        onOk={handleSubmit}
        onCancel={() => setModalOpen(false)}
        destroyOnHidden
        width={560}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="departureDate" label="出发日期" rules={[{ required: true, message: '请输入出发日期' }]}>
            <Input placeholder="YYYY-MM-DD" />
          </Form.Item>
          <Form.Item name="departureLocation" label="出发地" rules={[{ required: true, message: '请输入出发地' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="destinationLocation" label="目的地" rules={[{ required: true, message: '请输入目的地' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="arrivalDate" label="到达日期" rules={[{ required: true, message: '请输入到达日期' }]}>
            <Input placeholder="YYYY-MM-DD" />
          </Form.Item>
          <Form.Item name="vehicle" label="运输车辆">
            <Input />
          </Form.Item>
          <Form.Item name="hiveCount" label="蜂箱数">
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="cost" label="费用(元)">
            <InputNumber min={0} step={0.01} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="notes" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
