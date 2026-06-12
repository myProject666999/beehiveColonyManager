import { Routes, Route } from 'react-router-dom';
import MainLayout from './layouts/MainLayout';
import ApiaryList from './pages/ApiaryList';
import HiveList from './pages/HiveList';
import InspectionList from './pages/InspectionList';
import HarvestList from './pages/HarvestList';
import MigrationList from './pages/MigrationList';

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<MainLayout />}>
        <Route index element={<ApiaryList />} />
        <Route path="apiaries" element={<ApiaryList />} />
        <Route path="hives" element={<HiveList />} />
        <Route path="inspections" element={<InspectionList />} />
        <Route path="harvests" element={<HarvestList />} />
        <Route path="migrations" element={<MigrationList />} />
      </Route>
    </Routes>
  );
}
