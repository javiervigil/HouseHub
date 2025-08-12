import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null, // Objeto con los datos del usuario
    token: null, // El token de autenticación
    isAuthenticated: false, // Un booleano que indica el estado
  }),

  getters: {
    isLoggedIn: (state) => state.isAuthenticated,
  },

  actions: {
    login(userData, token) {
      this.user = userData;
      this.token = token;
      this.isAuthenticated = true;

      // Opcional: guardar el token en el almacenamiento local para persistencia
      localStorage.setItem('authToken', token);
    },

    logout() {
      this.user = null;
      this.token = null;
      this.isAuthenticated = false;
      localStorage.removeItem('authToken');
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