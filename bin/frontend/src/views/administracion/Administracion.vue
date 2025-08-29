<template>
    <div class="card">
        <div class="card">
            <h2 style="display: flex; align-items: center; gap: 10px;"><i class="pi pi-objects-column" style="font-size: 2rem" /> Administracion</h2>
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
                            <HistoricoLote :selectedCondominio="selectedItem" />
                        </p>
                    </TabPanel>
                    <TabPanel value="1">
                        <p class="m-0">
                            <EdoCuentaCondominio :selectedCondominio="selectedItem" />
                        </p>
                    </TabPanel>
                    <TabPanel value="2">
                        <p class="m-0">
                            <TipoCuot :selectedCondominio="selectedItem" />
                        </p>
                    </TabPanel>
                    <TabPanel value="3">
                        <p class="m-0">
                            <BitacoraAcces :selectedCondominio="selectedItem" />
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
import HistoricoLote from './HistoricoLote.vue';
import EdoCuentaCondominio from './EdoCuentaCondominio.vue';
import BitacoraAccesos from './BitacoraAccesos.vue';


export default {
    components: {
        Tabs, TabList, Tab, TabPanels, TabPanel, Button, Select, HistoricoLote, EdoCuentaCondominio, BitacoraAccesos

    },
    data() {
        return {
            authStore: useAuthStore(),
            selectedItem: null,
            condominiosList: [],
            userData: null,
            items: [
                { id: '0', label: 'Historico por Lote', icon: 'pi pi-arrow-right-arrow-left' },
                { id: '1', label: 'Estado de Cuenta por Condominio', icon: 'pi pi-calculator' },
                { id: '2', label: 'Cuotas', icon: 'pi pi-file-export' },
                { id: '3', label: 'Bitacora Acceso', icon: 'pi pi-address-book' }
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
