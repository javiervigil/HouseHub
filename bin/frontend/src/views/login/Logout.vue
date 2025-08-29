<template>
    <button @click="handleLogout">Cerrar Sesión</button>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const authStore = useAuthStore();

const handleLogout = () => {
    authStore.logout();
    router.push({ name: 'login' }); 
};
</script>