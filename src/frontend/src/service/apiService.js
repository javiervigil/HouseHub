import axios from 'axios';


const API_URL = 'http://localhost:8080/HouseHub'; // Replace with your actual API URL //desarrollo
//const API_URL = '/HouseHub'; // Replace with your actual API URL // produccion

// Create a reusable Axios instance
const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default {
  logInputData(data) {
    console.log('Envio de informacion Axios: ' + JSON.stringify(data));
  },

  login(data) {
    this.logInputData(data);
    return apiClient.post("/login", data);
  },

  //----------- Lotes
  getLotes() {
    return apiClient.get("/lotes");
  },
  getLote(id) {
    this.logInputData(id);
    return apiClient.get("/lotes/" + id);
  },
  createLote(data) {
    this.logInputData(data);
    data.lotes = null;
    data.userAdmins = null;
    return apiClient.post("/lotes", data)
  },
  deleteLote(id) {
    this.logInputData(id);
    return apiClient.delete("/lotes/" + id);
  },
  updateLote(data) {
    this.logInputData(data);
    return apiClient.put("/lotes", data);
  },
  createLote(data) {
    this.logInputData(data);
    return apiClient.post("/lotes", data);
  },
  /// ----lotes por condominio
  getLotesByCondominio(id) {
    this.logInputData(id);
    return apiClient.get("/lotes/bycondo/" + id);
  },




  //----------- Lote Types
  getTiposLote() {
    return apiClient.get("/lotetypes");
  },
  getTipoLote(id) {
    this.logInputData(id);
    return apiClient.get("/lotetypes/" + id);
  },
  createTipoLote(data) {
    this.logInputData(data);
    return apiClient.post("/lotetypes", data);
  },
  deleteTipoLote(id) {
    this.logInputData(id);
    return apiClient.delete("/lotetypes/" + id);
  },
  updateTipoLote(data) {
    this.logInputData(data);
    return apiClient.put("/lotetypes", data);
  },
  createTipoLote(data) {
    this.logInputData(data);
    return apiClient.post("/lotetypes", data);
  },
  getTiposLoteByCondominio(id) {
    return apiClient.get("/lotetypes/bycondo/" + id);
  },


  //----------- Cuota Types
  getTiposCuota() {
    return apiClient.get("/cuotatypes");
  },
  getTipoCuota(id) {
    this.logInputData(id);
    return apiClient.get("/cuotatypes/" + id);
  },
  createTipoCuota(data) {
    this.logInputData(data);
    return apiClient.post("/cuotatypes", data);
  },
  deleteTipoCuota(id) {
    this.logInputData(id);
    return apiClient.delete("/cuotatypes/" + id);
  },
  updateTipoCuota(data) {
    this.logInputData(data);
    return apiClient.put("/cuotatypes", data);
  },
  createTipoCuota(data) {
    this.logInputData(data);
    return apiClient.post("/cuotatypes", data);
  },
  getTiposCuotaByCondominio(id) {
    return apiClient.get("/cuotatypes/bycondo/" + id);
  },




  //------------ Tipos de Egresos
  //----------- Cuota Types
  getTiposEgreso() {
    return apiClient.get("/egresotypes");
  },
  getTipoEgreso(id) {
    this.logInputData(id);
    return apiClient.get("/egresotypes/" + id);
  },
  createTipoEgreso(data) {
    this.logInputData(data);
    return apiClient.post("/egresotypes", data);
  },
  deleteTipoEgreso(id) {
    this.logInputData(id);
    return apiClient.delete("/egresotypes/" + id);
  },
  updateTipoEgreso(data) {
    this.logInputData(data);
    return apiClient.put("/egresotypes", data);
  },
  createTipoEgreso(data) {
    this.logInputData(data);
    return apiClient.post("/egresotypes", data);
  },
  getTiposEgresoByCondominio(id) {
    return apiClient.get("/egresotypes/bycondo/" + id);
  },



  //----------- Contactos
  getUsuarios() {
    return apiClient.get("/usuarios");
  },
  getUsuario(id) {
    return apiClient.get("/usuarios/" + id);
  },
  createUsuario(data) {
    this.logInputData(data);
    return apiClient.post("/usuarios", data);
  },
  deleteUsuario(id) {
    this.logInputData(id);
    return apiClient.delete("/usuarios/" + id);
  },
  updateUsuario(data) {
    this.logInputData(data);
    return apiClient.put("/usuarios", data);
  },
  createUsuario(data) {
    this.logInputData(data);
    return apiClient.post("/usuarios", data);
  },
  getUsuariosByCondominio(id) {
    this.logInputData(id);
    return apiClient.get("/usuarios/bycondo/" + id);
  },


  //----------- Condominios
  getCondominios() {
    return apiClient.get("/condominios");
  },
  getCondominio(id) {
    this.logInputData(id);
    return apiClient.get("/condominios/" + id);
  },
  createCondominio(data) {
    this.logInputData(data);
    return apiClient.post("/condominios", data);
  },
  deleteCondominio(id) {
    this.logInputData(id);
    return apiClient.delete("/condominios/" + id);
  },
  updateCondominio(data) {
    this.logInputData(data);
    return apiClient.put("/condominios", data);
  },
  createCondominio(data) {
    this.logInputData(data);
    return apiClient.post("/condominios", data);
  },
  getCondominiosByUsr(id) {
    this.logInputData(id);
    return apiClient.get("/condominios/byuser/" + id);
  },


  //----------- TransaccionesLote
  // getTransaccionesLote() {
  //   return apiClient.get("/transaccioneslote");
  // },
  getTransaccionesLote(id) {
    this.logInputData(id);
    return apiClient.get("/transaccioneslote/" + id);
  },
  createTransaccionesLote(data) {
    this.logInputData(data);
    return apiClient.post("/transaccioneslote", data);
  },
  deleteTransaccionesLote(id) {
    this.logInputData(id);
    return apiClient.delete("/transaccioneslote/" + id);
  },
  updateTransaccionesLote(data) {
    this.logInputData(data);
    return apiClient.put("/transaccioneslote", data);
  },
  getTransaccionesByLote(id) {
    this.logInputData(id);
    return apiClient.get("/transaccioneslote/bylote/" + id);
  },

  getTransaccionesLoteTotalesByCondominio(id) {
    this.logInputData(id);
    return apiClient.get("/transaccioneslote/totales/" + id);
  },




  //------------------TransaccionesLoteType
  // getTransaccionesLoteTypesByType(id) {
  //   this.logInputData(id);
  //   return apiClient.get("/transaccioneslotetypes/bytype/" + id);
  // },
  getTransaccionesLoteTypesByCondominio(idCondo, idType) {
    this.logInputData(idCondo + ", " + idType);
    return apiClient.get("/transaccioneslotetypes/bycondobytype/" + idCondo + "/" + idType);
  },
  createTransaccionesLoteTypes(data) {
    this.logInputData(data);
    return apiClient.post("/transaccioneslotetypes", data);
  },
  deleteTransaccionesLoteTypes(id) {
    this.logInputData(id);
    return apiClient.delete("/transaccioneslotetypes/" + id);
  },
  updateTransaccionesLoteTypes(data) {
    this.logInputData(data);
    return apiClient.put("/transaccioneslotetypes", data);
  },



  //----------------------Bitacora Acceso

  getBitacoraAccesos() {
    return apiClient.get("/bitacoraacceso");
  },
  getBitacoraAcceso(id) {
    this.logInputData(id);
    return apiClient.get("/bitacoraacceso/" + id);
  },
  createBitacoraAcceso(data) {
    this.logInputData(data);
    return apiClient.post("/bitacoraacceso", data);
  },
  deleteBitacoraAcceso(id) {
    this.logInputData(id);
    return apiClient.delete("/bitacoraacceso/" + id);
  },
  updateBitacoraAcceso(data) {
    this.logInputData(data);
    return apiClient.put("/bitacoraacceso", data);
  },
  createBitacoraAcceso(data) {
    this.logInputData(data);
    return apiClient.post("/bitacoraacceso", data);
  },


  //////----------EgresosType

  getTiposEgresosByCondominio(id) {
    this.logInputData(id);
    return apiClient.get("/egresotypes/bycondo/" + id);
  },
  createEgresoTypes(data) {
    this.logInputData(data);
    return apiClient.post("/egresotypes", data);
  },
  deleteEgresoTypes(id) {
    this.logInputData(id);
    return apiClient.delete("/egresotypes/" + id);
  },
  updateEgresoTypes(data) {
    this.logInputData(data);
    return apiClient.put("/egresotypes", data);
  },


  //////----------Egresos

  getEgresosByCondominio(id) {
    this.logInputData(id);
    return apiClient.get("/egresos/bycondo/" + id);
  },
  getEgresos() {
    return apiClient.get("/egresos");
  },
  getEgreso(id) {
    this.logInputData(id);
    return apiClient.get("/egresos/" + id);
  },
  createEgreso(data) {
    this.logInputData(data);
    return apiClient.post("/egresos", data);
  },
  deleteEgreso(id) {
    this.logInputData(id);
    return apiClient.delete("/egresos/" + id);
  },
  updateEgreso(data) {
    this.logInputData(data);
    return apiClient.put("/egresos", data);
  },

};