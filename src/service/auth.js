import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => {
    return {
      userData: {
        email: '',
        name: '',
        rol: null
      },
      token: null, // El token de autenticación
      isAuthenticated: false, // Un booleano que indica el estado
    }
  },

  getters: {
    isLoggedIn: (state) => state.isAuthenticated,
  },

  actions: {
    login(userData, token) {
      this.userData = userData;
      this.token = token;
      this.isAuthenticated = true;

      // Opcional: guardar el token en el almacenamiento local para persistencia
      localStorage.setItem('authToken', this.token);
      localStorage.setItem('user', this.userData);
    },

    logout() {
      this.email = null;
      this.name = null;
      this.rol = 0;
      this.token = null;
      this.isAuthenticated = false;
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');
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