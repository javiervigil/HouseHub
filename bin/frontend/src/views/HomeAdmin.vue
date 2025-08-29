<template>
    <card style="width: 600px;  margin-top: 20px;">
        <template #title><b>Condominios administrados: </b> </template>
        <template #content>
            <DataTable v-model:selection="selectedItem" :value="condominiosList" size="small" dataKey="id" stripedRows>
                <Column selectionMode="single"></Column>
                <Column field="id" header="Id" hidden></Column>
                <Column field="name" header="Nombre"></Column>
                <Column field="description" header="Descripcion"></Column>
                <Column field="numLotes" header="# de Lotes"></Column>
                <Column field="image" header="Imagen"></Column>
            </DataTable>
        </template>
    </card>
</template>
<script>

import { useAuthStore } from '@/service/auth';
import apiService from '@/service/apiService';
import 'primeicons/primeicons.css';
import { Card, Column, DataTable } from 'primevue';


export default {

    components: {
        Card, DataTable, Column
    },
    data() {
        return {
            authStore: useAuthStore(),
            condominiosList: [],
            userData: null,
            selectedItem: null
        }
    },
    methods: {
        async refreshTable() {
            if (this.userData && this.userData.id) {
                this.condominiosList = (await apiService.getCondominiosByUsr(this.userData.id)).data;
            }
        },
    },
    mounted() {
        this.userData = this.authStore.getUserData();
        this.refreshTable();
    }
}
</script>

<style scope></style>
