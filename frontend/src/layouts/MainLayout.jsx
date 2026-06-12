import { useState } from 'react';
import { Outlet, useNavigate, useLocation } from 'react-router-dom';
import { Layout, Menu } from 'antd';
import {
  HomeOutlined,
  BorderInnerOutlined,
  FileSearchOutlined,
  DollarOutlined,
  SwapOutlined,
} from '@ant-design/icons';

const { Header, Sider, Content } = Layout;

const menuItems = [
  { key: '/apiaries', icon: <HomeOutlined />, label: '蜂场管理' },
  { key: '/hives', icon: <BorderInnerOutlined />, label: '蜂箱管理' },
  { key: '/inspections', icon: <FileSearchOutlined />, label: '检查记录' },
  { key: '/harvests', icon: <DollarOutlined />, label: '取蜜记录' },
  { key: '/migrations', icon: <SwapOutlined />, label: '转场记录' },
];

export default function MainLayout() {
  const [collapsed, setCollapsed] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const selectedKey = menuItems.find((item) =>
    location.pathname.startsWith(item.key)
  )?.key || '/apiaries';

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider
        collapsible
        collapsed={collapsed}
        onCollapse={setCollapsed}
        theme="dark"
      >
        <div
          style={{
            height: 48,
            margin: 16,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}
        >
          <span
            style={{
              color: '#fff',
              fontSize: collapsed ? 14 : 16,
              fontWeight: 'bold',
              whiteSpace: 'nowrap',
            }}
          >
            {collapsed ? '蜂' : '蜂群管理'}
          </span>
        </div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[selectedKey]}
          items={menuItems}
          onClick={({ key }) => navigate(key)}
        />
      </Sider>
      <Layout>
        <Header
          style={{
            background: '#fff',
            padding: '0 24px',
            display: 'flex',
            alignItems: 'center',
            boxShadow: '0 1px 4px rgba(0,0,0,0.08)',
          }}
        >
          <h2 style={{ margin: 0, fontSize: 20, color: '#333' }}>
            蜜蜂蜂群养殖管理系统
          </h2>
        </Header>
        <Content
          style={{
            margin: 24,
            padding: 24,
            background: '#fff',
            borderRadius: 8,
            minHeight: 280,
          }}
        >
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
}
