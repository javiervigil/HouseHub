<template>
  <aside class="sidebar">
    <nav class="sidebar-nav">
      <!-- Menu Admin -->
      <ul v-if="userData.rol === 2">
        <li>
          <router-link :to="{ name: 'homeadmin' }"><Button icon="pi pi-home" v-tooltip="'Home'" /></router-link>
        </li>
        <li>
          <router-link :to="{ name: 'finanzasa' }"><Button icon="pi pi-money-bill"
              v-tooltip="'Finanzas'" /></router-link>
        </li>
        <li>
          <router-link :to="{ name: 'administracion' }"><Button icon="pi pi-objects-column"
              v-tooltip="'Administracion'" /></router-link>
        </li>
        <li>
          <router-link :to="{ name: 'configuracion' }"><Button icon="pi pi-cog"
              v-tooltip="'Configuracion'" /></router-link>
        </li>
        <li>
          <router-link :to="{ name: 'about' }"><Button icon="pi pi-question-circle"
              v-tooltip="'Acerca de'" /></router-link>
        </li>
        <li>
          <Button icon="pi pi-sign-out" v-tooltip="'Cerrar Sesion'" @click="handleLogout" />
        </li>
      </ul>

      <!-- Menu Usuario -->
      <ul v-if="userData.rol === 1">
        <li>
          <router-link :to="{ name: 'homeuser' }"><Button icon="pi pi-home" v-tooltip="'Home'" /></router-link>
        </li>
        <li>
          <router-link :to="{ name: 'finanzasu' }"><Button icon="pi pi-chart-line"
              v-tooltip="'Finanzas'" /></router-link>
        </li>
        <li>
          <router-link :to="{ name: 'about' }"><Button icon="pi pi-question-circle"
              v-tooltip="'Acerca de'" /></router-link>
        </li>
        <li>
          <Button icon="pi pi-sign-out" v-tooltip="'Cerrar Sesion'" @click="handleLogout" />
        </li>
      </ul>

      <!-- Menu Super Usuario -->
      <ul v-if="userData.rol === 3">
        <li>
          <router-link :to="{ name: 'homesuperuser' }"><Button icon="pi pi-home" v-tooltip="'Home'" /></router-link>
        </li>
        <li>
          <Button icon="pi pi-sign-out" v-tooltip="'Cerrar Sesion'" @click="handleLogout" />
        </li>
      </ul>
    </nav>
  </aside>
</template>

<script>
import { Button } from 'primevue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/service/auth';
import 'primeicons/primeicons.css';

export default {

  components: {
    Button
  },
  data() {
    return {
      router: useRouter(),
      authStore: useAuthStore(),
      userData: {
        id: '',
        email: '',
        name: '',
        rol: null
      },

    }
  },
  methods: {
    handleLogout() {
      this.authStore.logout();
      this.$router.push({ name: 'login' })
    }
  },
  mounted() {
    this.userData = this.authStore.getUserData();
  }
}
</script>



<style scoped>
.sidebar {
  width: 70px;
  /* Ancho fijo del sidebar */
  margin-top: 100px;
  padding: 10px;
  display: flex;
  flex-direction: column;

}

.sidebar-header {
  align-items: center;

}

.sidebar-logo {
  border-radius: 50%;
}

.sidebar-title {
  font-size: 1.25rem;
  font-weight: bold;
}

.sidebar-nav {
  margin-top: 1rem;
}

.sidebar-nav ul {
  list-style: none;
  padding: 0;
}

.sidebar-nav li {
  margin-bottom: 0.5rem;
}



/* Para pantallas más pequeñas (modo móvil) */
@media (max-width: 768px) {
  .sidebar {
    width: 100%;
    height: auto;
  }
}
</style>