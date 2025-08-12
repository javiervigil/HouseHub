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
  login(email, password) {

    const user = {
      email: email,
      password: password,
    };
    return apiClient.post("/login", user);
  },
  //----------- Lotes
  getLotes() {
    return apiClient.get("/lotes");
  },
  getLote(id) {
    return apiClient.get("/lotes/" + id);
  },
  createLote(item) {
    console.log('createLote loteData ' + JSON.stringify(item));
    return apiClient.post("/lotes", item);
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
  getContactos() {
    return apiClient.get("/contactos");
  },
  getContacto(id) {
    return apiClient.get("/contactos/" + id);
  },
  createContacto(data) {
    return apiClient.post("/contactos", data);
  },
  deleteContacto(id) {
    return apiClient.delete("/contactos/" + id);
  },
  updateContacto(data) {
    return apiClient.put("/contactos", data);
  },
  createContacto(data) {
    return apiClient.post("/contactos", data);
  },
  getContactosShort() {
    return apiClient.get("/contactosshort");
  },

};