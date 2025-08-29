<template>
    <div class="card">
        <div class="card" >
            <h2 style="display: flex; align-items: center; gap: 10px;"><i class="pi pi-money-bill" style="font-size: 2rem" /> Finanzas</h2>
        </div>
        <div>
            <b style="color: var(--p-tabs-tab-active-color);">Seleccione el condominio: </b>
            <Select v-model="selectedItem" :options="condominiosList" optionLabel="name" @change="refreshTable"
                style="width: 250px;" size="small" />

            <Button icon="pi pi-sync" variant="text" v-tooltip.top="'Actualizar lista'" @click="refreshTable"
                style="margin-right: 20px;" /><!-- refresh -->
        </div>
        <div>
            <Tabs>
                <TabList>
                    <Tab v-for="tab in items" :key="tab.id" :value="tab.id">
                        <div>
                            <i :class="tab.icon" style="margin-right: 10px;" />
                            {{ tab.label }}
                        </div>
                    </Tab>
                </TabList>
                <TabPanels>
                    <TabPanel value="0">
                        <p class="m-0">
                           <Egresos :selectedCondominio="selectedItem" />
                        </p>
                    </TabPanel>
                    <TabPanel value="1">
                        <p class="m-0">
                            
                        </p>
                    </TabPanel>
                    <TabPanel value="2">
                        <p class="m-0">
                            
                        </p>
                    </TabPanel>
                    <TabPanel value="3">
                        <p class="m-0">
                            
                        </p>
                    </TabPanel>
                </TabPanels>
            </Tabs>
        </div>
    </div>
</template>

<script>
import { useAuthStore } from '@/service/auth';
import apiService from '@/service/apiService';
import { Button, Select, Tab, TabList, TabPanel, TabPanels, Tabs } from 'primevue';
import Egresos from './Egresos.vue';



export default {
    components: {
        Tabs, TabList, Tab, TabPanels, TabPanel, Button, Select, Egresos

    },
    data() {
        return {
            authStore: useAuthStore(),
            selectedItem: null,
            condominiosList: [],
            userData: null,
            items: [
                { id: '0', label: 'Egresos', icon: 'pi pi-arrow-right' },
                { id: '1', label: 'Balance', icon: 'pi pi-sitemap' },
                { id: '2', label: 'Cuotas', icon: 'pi pi-file-export' },
                { id: '3', label: 'Bitacora Acceso', icon: 'pi pi-info-circle' }
            ]
        }
    },
    methods: {
        async refreshTable() {
            if (this.userData) {
                try {
                    this.condominiosList = (await apiService.getCondominiosByUsr(this.userData.id)).data;
                } catch (error) {
                    console.error("Failed to fetch getCondominiosByUsr:", error);
                    this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
                }
            }
        },
        async getUserData() {
            try {
                this.userData = this.authStore.getUserData();
            } catch (error) {
                console.error("Failed to fetch getUserData:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        }
    },
    mounted() {
        this.getUserData()
        this.refreshTable();
    }
}
</script>

<style scope>
.internal {
    margin: 0;
    padding: 0;

}

.internalHeader {
    margin: 0;
    padding: 0;
}
</style>
