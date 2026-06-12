import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

request.interceptors.response.use(
  (res) => res.data,
  (err) => Promise.reject(err)
);

const apiaries = {
  list: () => request.get('/apiaries'),
  get: (id) => request.get(`/apiaries/${id}`),
  create: (data) => request.post('/apiaries', data),
  update: (id, data) => request.put(`/apiaries/${id}`, data),
  delete: (id) => request.delete(`/apiaries/${id}`),
};

const hives = {
  list: () => request.get('/hives'),
  getByApiary: (apiaryId) => request.get(`/apiaries/${apiaryId}/hives`),
  create: (data) => request.post('/hives', data),
  update: (id, data) => request.put(`/hives/${id}`, data),
  delete: (id) => request.delete(`/hives/${id}`),
};

const inspections = {
  list: () => request.get('/inspections'),
  getByHive: (hiveId) => request.get(`/hives/${hiveId}/inspections`),
  create: (data) => request.post('/inspections', data),
  update: (id, data) => request.put(`/inspections/${id}`, data),
  delete: (id) => request.delete(`/inspections/${id}`),
};

const harvests = {
  list: () => request.get('/harvests'),
  getByHive: (hiveId) => request.get(`/hives/${hiveId}/harvests`),
  create: (data) => request.post('/harvests', data),
  update: (id, data) => request.put(`/harvests/${id}`, data),
  delete: (id) => request.delete(`/harvests/${id}`),
};

const migrations = {
  list: () => request.get('/migrations'),
  getByApiary: (apiaryId) => request.get(`/apiaries/${apiaryId}/migrations`),
  create: (data) => request.post('/migrations', data),
  update: (id, data) => request.put(`/migrations/${id}`, data),
  delete: (id) => request.delete(`/migrations/${id}`),
};

const nectarSources = {
  list: () => request.get('/nectar-sources'),
  getByApiary: (apiaryId) => request.get(`/apiaries/${apiaryId}/nectar-sources`),
  create: (data) => request.post('/nectar-sources', data),
  update: (id, data) => request.put(`/nectar-sources/${id}`, data),
  delete: (id) => request.delete(`/nectar-sources/${id}`),
};

export { apiaries, hives, inspections, harvests, migrations, nectarSources };
