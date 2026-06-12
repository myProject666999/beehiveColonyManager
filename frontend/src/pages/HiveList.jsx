import { useState, useEffect, useCallback } from 'react';
import { Table, Button, Modal, Form, Input, InputNumber, Select, Tag, Space, Popconfirm, message, List } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { apiaries, hives } from '../api';

const statusConfig = {
  active: { color: 'green', label: '正常' },
  weak: { color: 'orange', label: '弱势' },
  dead: { color: 'red', label: '死亡' },
};

const queenSourceMap = {
  self_bred: '自育',
  purchased: '外购',
};

export default function HiveList() {
  const [apiaryList, setApiaryList] = useState([]);
  const [selectedApiary, setSelectedApiary] = useState(null);
  const [hiveData, setHiveData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form] = Form.useForm();

  const fetchApiaries = useCallback(async () => {
    try {
      const res = await apiaries.list();
      const list = Array.isArray(res) ? res : res.data || [];
      setApiaryList(list);
      if (list.length > 0 && !selectedApiary) {
        setSelectedApiary(list[0].id);
      }
    } catch {
      message.error('获取蜂场列表失败');
    }
  }, [selectedApiary]);

  const fetchHives = useCallback(async () => {
    if (!selectedApiary) return;
    setLoading(true);
    try {
      const res = await hives.getByApiary(selectedApiary);
      setHiveData(Array.isArray(res) ? res : res.data || []);
    } catch {
      message.error('获取蜂箱列表失败');
    }
    setLoading(false);
  }, [selectedApiary]);

  useEffect(() => {
    fetchApiaries();
  }, [fetchApiaries]);

  useEffect(() => {
    fetchHives();
  }, [fetchHives]);

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
      await hives.delete(id);
      message.success('删除成功');
      fetchHives();
    } catch {
      message.error('删除失败');
    }
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      const payload = { ...values, apiaryId: selectedApiary };
      if (editing) {
        await hives.update(editing.id, payload);
        message.success('更新成功');
      } else {
        await hives.create(payload);
        message.success('创建成功');
      }
      setModalOpen(false);
      fetchHives();
    } catch {
      message.error('操作失败');
    }
  };

  const columns = [
    { title: '编号', dataIndex: 'hiveNumber', key: 'hiveNumber', width: 120 },
    { title: '蜂王来源', dataIndex: 'queenSource', key: 'queenSource', width: 100, render: (v) => queenSourceMap[v] || v },
    { title: '蜂王年份', dataIndex: 'queenYear', key: 'queenYear', width: 100 },
    { title: '蜂王品种', dataIndex: 'queenBreed', key: 'queenBreed', width: 120 },
    { title: '工蜂数', dataIndex: 'workerBeeCount', key: 'workerBeeCount', width: 100 },
    { title: '巢框数', dataIndex: 'frameCount', key: 'frameCount', width: 90 },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      width: 90,
      render: (status) => {
        const cfg = statusConfig[status] || { color: 'default', label: status };
        return <Tag color={cfg.color}>{cfg.label}</Tag>;
      },
    },
    { title: '备注', dataIndex: 'description', key: 'description' },
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
    <div style={{ display: 'flex', gap: 16 }}>
      <div
        style={{
          width: 240,
          border: '1px solid #f0f0f0',
          borderRadius: 8,
          padding: 12,
          flexShrink: 0,
          maxHeight: 'calc(100vh - 180px)',
          overflowY: 'auto',
        }}
      >
        <div style={{ fontWeight: 'bold', marginBottom: 12 }}>蜂场列表</div>
        <List
          size="small"
          dataSource={apiaryList}
          renderItem={(item) => (
            <List.Item
              style={{
                cursor: 'pointer',
                background: selectedApiary === item.id ? '#e6f7ff' : 'transparent',
                borderRadius: 4,
                padding: '8px 12px',
              }}
              onClick={() => setSelectedApiary(item.id)}
            >
              {item.name}
            </List.Item>
          )}
        />
      </div>

      <div style={{ flex: 1 }}>
        <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <span style={{ fontWeight: 'bold', fontSize: 16 }}>
            {apiaryList.find((a) => a.id === selectedApiary)?.name || ''} - 蜂箱列表
          </span>
          <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd} disabled={!selectedApiary}>
            新增蜂箱
          </Button>
        </div>
        <Table
          rowKey="id"
          columns={columns}
          dataSource={hiveData}
          loading={loading}
        />
      </div>

      <Modal
        title={editing ? '编辑蜂箱' : '新增蜂箱'}
        open={modalOpen}
        onOk={handleSubmit}
        onCancel={() => setModalOpen(false)}
        destroyOnHidden
      >
        <Form form={form} layout="vertical">
          <Form.Item name="hiveNumber" label="蜂箱编号" rules={[{ required: true, message: '请输入编号' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="queenSource" label="蜂王来源">
            <Select
              options={[
                { value: 'self_bred', label: '自育' },
                { value: 'purchased', label: '外购' },
              ]}
            />
          </Form.Item>
          <Form.Item name="queenYear" label="蜂王年份">
            <InputNumber min={2000} max={2099} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="queenBreed" label="蜂王品种">
            <Input />
          </Form.Item>
          <Form.Item name="workerBeeCount" label="工蜂数量">
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="frameCount" label="巢框数量">
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="status" label="状态" rules={[{ required: true, message: '请选择状态' }]}>
            <Select
              options={[
                { value: 'active', label: '正常' },
                { value: 'weak', label: '弱势' },
                { value: 'dead', label: '死亡' },
              ]}
            />
          </Form.Item>
          <Form.Item name="description" label="备注">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
