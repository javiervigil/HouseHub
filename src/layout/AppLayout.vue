<template>
    <div class="layout-container">
        <SideBar />
        <main class="content-container">
            <header class="main-header">
                <img src="../assets/logoRP.jpg" alt="Home Hub" width="70px">
                <h1>Home Hub</h1>
            </header>
            <div class="main-content">
                <router-view />
            </div>
            <Footer />
        </main>
    </div>
    <Toast />
</template>

<script setup>
import SideBar from '@/components/SideBar.vue';

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
    padding: 10px;
    min-height: 100vh;
}

.main-header {
    background-color: #ffffff;
    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    display: flex;
    align-items: center;
    padding: 5px;
}

.main-header h1 {
    margin-left: 20px;
    font-size: 2rem;
}




.main-content {
    
    border-radius: 10px;
    background-color: white;

    border-radius: 10px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    padding: 20px;
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
