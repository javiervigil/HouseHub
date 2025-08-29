import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  persist: true,
  state: () => {
    return {
      userData: null,
      condominiosList: null,
      token: null, // El token de autenticación
      isAuthenticated: false, // Un booleano que indica el estado
    }
  },

  getters: {
    isLoggedIn: (state) => state.isAuthenticated,
  },

  actions: {
    login(userData) {
      this.isAuthenticated = true;
      this.userData = userData;
      localStorage.setItem('authToken', userData.token);
      localStorage.setItem('user', JSON.stringify(userData));
    },

    setCondminiosList(list) {
      this.condominiosList = list;
      localStorage.setItem('condominiosList', JSON.stringify(this.condominiosList));
    },

    getCondminiosList() {
      return JSON.parse(localStorage.getItem('condominiosList'));

    },

    getUserData() {
      return JSON.parse(localStorage.getItem('user'));
    },

    getToket() {
      return localStorage.setItem('authToken');
    },

    logout() {
      // this.email = null;
      // this.name = null;
      // this.rol = 0;
      // this.token = null;
      this.isAuthenticated = false;
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');
      this.$reset();
    },

    // Acción para cargar el estado desde el almacenamiento local al iniciar la app
    checkAuthStatus() {
      const storedToken = localStorage.getItem('authToken');
      if (storedToken) {
        this.token = storedToken;
        this.isAuthenticated = true;
        // Aquí podrías hacer una petición a la API para obtener los datos del usuario
        // basándote en el token
      }
    }
  },
})