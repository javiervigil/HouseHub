import axios from 'axios';

const API_URL = 'http://localhost:8080/HouseHub'; // Replace with your actual API URL

// Create a reusable Axios instance
const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default {
  loginAdmin(data) {
    return apiClient.post("/loginuser", data);
  },
  loginUser(data) {
    return apiClient.post("/logincontact", data);
  },

  //----------- Lotes
  getLotes() {
    return apiClient.get("/lotes");
  },
  getLote(id) {
    return apiClient.get("/lotes/" + id);
  },
  createLote(item) {
    return apiClient.post("/lotes", item)
  },
  deleteLote(id) {
    return apiClient.delete("/lotes/" + id);
  },
  updateLote(item) {
    return apiClient.put("/lotes", item);
  },
  createLote(item) {
    return apiClient.post("/lotes", item);
  },


  //----------- Lote Types
  getTiposLote() {
    return apiClient.get("/lotetypes");
  },
  getTipoLote(id) {
    return apiClient.get("/lotetypes/" + id);
  },
  createTipoLote(data) {
    return apiClient.post("/lotetypes", data);
  },
  deleteTipoLote(id) {
    return apiClient.delete("/lotetypes/" + id);
  },
  updateTipoLote(data) {
    return apiClient.put("/lotetypes", data);
  },
  createTipoLote(data) {
    return apiClient.post("/lotetypes", data);
  },


  //----------- Cuota Types
  getTiposCuota() {
    return apiClient.get("/cuotatypes");
  },
  getTipoCuota(id) {
    return apiClient.get("/cuotatypes/" + id);
  },
  createTipoCuota(data) {
    return apiClient.post("/cuotatypes", data);
  },
  deleteTipoCuota(id) {
    return apiClient.delete("/cuotatypes/" + id);
  },
  updateTipoCuota(data) {
    return apiClient.put("/cuotatypes", data);
  },
  createTipoCuota(data) {
    return apiClient.post("/cuotatypes", data);
  },


  //----------- Contactos
  getUsuarios() {
    return apiClient.get("/usuarios");
  },
  getUsuariosByRole(id) {
    return apiClient.get("/usuarios/rol/" + id);
  },
  getUsuario(id) {
    return apiClient.get("/usuarios/" + id);
  },
  createUsuario(item) {
    return apiClient.post("/usuarios", item);
  },
  deleteUsuario(id) {
    return apiClient.delete("/usuarios/" + id);
  },
  updateUsuario(item) {
    return apiClient.put("/usuarios", item);
  },
  createUsuario(item) {
    return apiClient.post("/usuarios", item);
  },


  //----------- Condominios
  getCondominios() {
    return apiClient.get("/condominios");
  },
  getCondominio(id) {
    return apiClient.get("/condominios/" + id);
  },
  createCondominio(item) {
    return apiClient.post("/condominios", item);
  },
  deleteCondominio(id) {
    return apiClient.delete("/condominios/" + id);
  },
  updateCondominio(item) {
    return apiClient.put("/condominios", item);
  },
  createCondominio(item) {
    return apiClient.post("/condominios", item);
  },


};