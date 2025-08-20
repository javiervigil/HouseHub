<template>
    <div class="layout-container">
        <SideBar />
        <main class="content-container">
            <header class="main-header">
                <div class="main ">
                    <img src="../assets/logoRP.jpg" alt="Home Hub" width="70px" height="70px">
                    <h1>Home Hub</h1>
                </div>
                <div class="main" style="padding-right: 50px;">
                    <h3>Bienvenido: {{ userData.name }}<br />
                        Perfil: {{ obtieneTexto }}</h3>
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
import SideBar from '@/components/SideBar.vue';
import { useAuthStore } from '@/service/auth';

export default {

    components: {
        SideBar
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
        this.userData = this.authStore.userData;
    }
}

</script>

<style>
body {
    background: #DDDDDD;
    background: linear-gradient(90deg, rgba(221, 221, 221, 1) 0%, rgba(255, 255, 255, 1) 51%, rgba(221, 221, 221, 1) 100%);
}

h1,
h2,
h3 {
    color: var(--p-primary-color);
}

.layout-container {
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

.main {
    display: flex;
    
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
}

/* Para pantallas más pequeñas (modo móvil) */
@media (max-width: 768px) {
    .layout-container {
        flex-direction: column;
    }
}
</style>
