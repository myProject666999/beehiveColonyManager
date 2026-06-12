import { useState, useEffect, useCallback } from 'react';
import { Table, Button, Modal, Form, Input, InputNumber, Space, Popconfirm, message } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { apiaries, nectarSources } from '../api';

const seasonMap = { spring: '春季', summer: '夏季', autumn: '秋季', winter: '冬季' };

export default function ApiaryList() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form] = Form.useForm();
  const [expandedKeys, setExpandedKeys] = useState([]);
  const [nectarData, setNectarData] = useState({});
  const [nsModalOpen, setNsModalOpen] = useState(false);
  const [nsEditing, setNsEditing] = useState(null);
  const [nsForm] = Form.useForm();
  const [currentApiaryId, setCurrentApiaryId] = useState(null);

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const res = await apiaries.list();
      setData(Array.isArray(res) ? res : res.data || []);
    } catch {
      message.error('获取蜂场列表失败');
    }
    setLoading(false);
  }, []);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const fetchNectarSources = useCallback(async (apiaryId) => {
    try {
      const res = await nectarSources.getByApiary(apiaryId);
      setNectarData((prev) => ({
        ...prev,
        [apiaryId]: Array.isArray(res) ? res : res.data || [],
      }));
    } catch {
      message.error('获取蜜源信息失败');
    }
  }, []);

  const handleExpand = (expanded, record) => {
    setExpandedKeys(expanded ? [record.id] : []);
    if (expanded && !nectarData[record.id]) {
      fetchNectarSources(record.id);
    }
  };

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
      await apiaries.delete(id);
      message.success('删除成功');
      fetchData();
    } catch {
      message.error('删除失败');
    }
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      if (editing) {
        await apiaries.update(editing.id, values);
        message.success('更新成功');
      } else {
        await apiaries.create(values);
        message.success('创建成功');
      }
      setModalOpen(false);
      fetchData();
    } catch {
      message.error('操作失败');
    }
  };

  const handleNsAdd = (apiaryId) => {
    setCurrentApiaryId(apiaryId);
    setNsEditing(null);
    nsForm.resetFields();
    setNsModalOpen(true);
  };

  const handleNsEdit = (record, apiaryId) => {
    setCurrentApiaryId(apiaryId);
    setNsEditing(record);
    nsForm.setFieldsValue(record);
    setNsModalOpen(true);
  };

  const handleNsDelete = async (id, apiaryId) => {
    try {
      await nectarSources.delete(id);
      message.success('删除成功');
      fetchNectarSources(apiaryId);
    } catch {
      message.error('删除失败');
    }
  };

  const handleNsSubmit = async () => {
    try {
      const values = await nsForm.validateFields();
      const payload = { ...values, apiaryId: currentApiaryId };
      if (nsEditing) {
        await nectarSources.update(nsEditing.id, payload);
        message.success('更新成功');
      } else {
        await nectarSources.create(payload);
        message.success('创建成功');
      }
      setNsModalOpen(false);
      fetchNectarSources(currentApiaryId);
    } catch {
      message.error('操作失败');
    }
  };

  const nectarColumns = [
    { title: '蜜源名称', dataIndex: 'name', key: 'name' },
    { title: '季节', dataIndex: 'season', key: 'season', render: (v) => seasonMap[v] || v },
    { title: '开始月份', dataIndex: 'startMonth', key: 'startMonth' },
    { title: '结束月份', dataIndex: 'endMonth', key: 'endMonth' },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button type="link" icon={<EditOutlined />} onClick={() => handleNsEdit(record, currentApiaryId || record.apiaryId)}>
            编辑
          </Button>
          <Popconfirm title="确定删除?" onConfirm={() => handleNsDelete(record.id, currentApiaryId || record.apiaryId)}>
            <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  const columns = [
    { title: '名称', dataIndex: 'name', key: 'name' },
    { title: '位置', dataIndex: 'location', key: 'location' },
    { title: '主打蜜源', dataIndex: 'mainNectarSource', key: 'mainNectarSource' },
    { title: '面积(亩)', dataIndex: 'area', key: 'area' },
    {
      title: '操作',
      key: 'action',
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
      <div style={{ marginBottom: 16 }}>
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
          新增蜂场
        </Button>
      </div>

      <Table
        rowKey="id"
        columns={columns}
        dataSource={data}
        loading={loading}
        expandable={{
          expandedRowKeys: expandedKeys,
          onExpand: handleExpand,
          expandedRowRender: (record) => (
            <div>
              <div style={{ marginBottom: 8, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <span style={{ fontWeight: 'bold' }}>季节蜜源</span>
                <Button size="small" type="primary" icon={<PlusOutlined />} onClick={() => handleNsAdd(record.id)}>
                  新增蜜源
                </Button>
              </div>
              <Table
                rowKey="id"
                columns={nectarColumns}
                dataSource={nectarData[record.id] || []}
                pagination={false}
                size="small"
              />
            </div>
          ),
        }}
      />

      <Modal
        title={editing ? '编辑蜂场' : '新增蜂场'}
        open={modalOpen}
        onOk={handleSubmit}
        onCancel={() => setModalOpen(false)}
        destroyOnHidden
      >
        <Form form={form} layout="vertical">
          <Form.Item name="name" label="名称" rules={[{ required: true, message: '请输入名称' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="location" label="位置" rules={[{ required: true, message: '请输入位置' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="mainNectarSource" label="主打蜜源">
            <Input />
          </Form.Item>
          <Form.Item name="area" label="面积(亩)">
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title={nsEditing ? '编辑蜜源' : '新增蜜源'}
        open={nsModalOpen}
        onOk={handleNsSubmit}
        onCancel={() => setNsModalOpen(false)}
        destroyOnHidden
      >
        <Form form={nsForm} layout="vertical">
          <Form.Item name="name" label="蜜源名称" rules={[{ required: true, message: '请输入蜜源名称' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="season" label="季节" rules={[{ required: true, message: '请选择季节' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="startMonth" label="开始月份" rules={[{ required: true, message: '请输入开始月份' }]}>
            <InputNumber min={1} max={12} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="endMonth" label="结束月份" rules={[{ required: true, message: '请输入结束月份' }]}>
            <InputNumber min={1} max={12} style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
