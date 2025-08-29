<template>
    <div class="layout-container">
        <SideBar class="sidebar" />
        <main class="main-content-wrapper">
            <header class="header">
                <div class="main">
                    <img src="../assets/logoRP.jpg" alt="Home Hub" class="logo">
                    <h1>Home Hub</h1>
                </div>
                <div class="main" style="padding-right: 50px;">
                    <h3>Bienvenido: {{ userData.name }} <br />
                        Perfil: {{ obtieneTexto }} </h3>
                </div>
            </header>
            <div class="main-content">
                <router-view />
            </div>
            <Footer />
        </main>
    </div>
</template>

<script>
import Footer from '@/components/Footer.vue';
import SideBar from '@/components/SideBar.vue';
import { useAuthStore } from '@/service/auth';

export default {

    components: {
        SideBar, Footer
    },
    data() {
        return {
            authStore: useAuthStore(),
            userData: {
                email: '',
                name: '',
                rol: null
            },
        }
    },
    computed: {
        obtieneTexto() {
            switch (this.userData.rol) {
                case 1:
                    return 'Usuario';
                case 2:
                    return 'Administrador';
                case 3:
                    return 'Super Administrador';
            }
        }
    },
    mounted() {
        console.log('AppLayout');
        this.userData = this.authStore.getUserData();
    }
}

</script>

<style>
/* body {
    background: #DDDDDD;
    background: linear-gradient(90deg, rgba(221, 221, 221, 1) 0%, rgba(255, 255, 255, 1) 51%, rgba(221, 221, 221, 1) 100%);
} */

:root {
    /* Variables para fácil personalización */
    --sidebar-width: 50px;
    --header-height: 60px;
    --padding-content: 20px;
    --border-radius: 10px;
    --background-color: #f0f2f5;
    --white-color: #ffffff;
    --shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

body {
    margin: 0;
    background: #DDDDDD;
    background: linear-gradient(90deg, rgba(221, 221, 221, 1) 0%, rgba(255, 255, 255, 1) 51%, rgba(221, 221, 221, 1) 100%);

}

h1,
h2,
h3,
b {
    color: var(--p-primary-color);
}

.main {
    display: flex;
    vertical-align: bottom;
}

/* Para pantallas más pequeñas (modo móvil) */
@media (max-width: 768px) {
    .layout-container {
        flex-direction: column;
    }
}


.layout-container {
    display: flex;
    min-height: 100vh;
}

/* Sidebar: Fijo a la izquierda con el ancho definido por la variable */
.sidebar {
    width: var(--sidebar-width);
    min-width: var(--sidebar-width);
    height: 100vh;
    position: fixed;
    margin-left: 5px;
    top: 0;
    left: 0;
    /* La barra lateral no tiene un fondo definido */
}

/* Contenido Principal: El wrapper que contiene el header y el main content */
.main-content-wrapper {
    flex-grow: 1;
    /* Ocupa todo el espacio restante */
    margin-left: var(--sidebar-width);
    /* Deja un espacio para el sidebar */
    padding: var(--padding-content);
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    height: 100vh;
    gap: var(--padding-content);
    /* Espacio entre el header y el main */
}

/* Header: Fondo blanco con bordes redondeados y sombra */
.header {
    height: var(--header-height);
    background-color: var(--white-color);
    border-radius: var(--border-radius);
    box-shadow: var(--shadow);
    display: flex;
    align-items: center;
    padding: 0 var(--padding-content);
    justify-content: space-between;
    vertical-align: bottom;
}

.logo {
    width: 70px;
    height: 50px;
    border-radius: 8px;
    margin-top: 15px;
    margin-right: 20px;
    align-self: baseline;
}

/* Main Content: El área de contenido principal, también con fondo blanco, bordes redondeados y sombra */
.main-content {
    background-color: var(--white-color);
    border-radius: var(--border-radius);
    box-shadow: var(--shadow);
    padding: var(--padding-content);
    flex-grow: 1;

    /* Permite que el contenido principal ocupe el espacio vertical restante */
}

/* Responsividad: Oculta el sidebar en pantallas pequeñas para optimizar el espacio */
@media (max-width: 768px) {
    .sidebar {
        display: none;
    }

    .main-content-wrapper {
        margin-left: 0;
        /* El contenido principal ocupa el 100% del ancho */
    }
}







/* .layout-container {
    display: flex;
    min-height: 100vh;

}

.content-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    justify-content: space-between;
    padding: 0px;
    min-height: 100vh;
}

.sidebar {
    background-color: transparent;
    min-width: 50px;
    height: 100vh;
    top: 0px;
    left: 0px;
    position: fixed;
}

.main {
    display: flex;
    vertical-align: bottom;
}

.main-header {
    background-color: #ffffff;
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    display: flex;
    align-items: center;
    padding: 5px;
    justify-content: space-between;
}

.main-header h1 {
    margin-left: 20px;

}

.main-content {
    border-radius: 10px;
    background-color: white;
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    padding-left: 20px;
    overflow-y: auto;
    justify-content: space-between;
    display: flex;
    flex-direction: column;
    height: 80%;
} */
</style>
