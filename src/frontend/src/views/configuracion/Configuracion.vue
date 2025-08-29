<template>
    <div class="card">
        <div class="card">
            <h2 style="display: flex; align-items: center; gap: 10px;"><i class="pi pi-cog" style="font-size: 2rem" /> Configuracion</h2>
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
                            <Lotes :selectedCondominio="selectedItem" />
                        </p>
                    </TabPanel>
                    <TabPanel value="1">
                        <p class="m-0">
                            <TipoLotes :selectedCondominio="selectedItem" />
                        </p>
                    </TabPanel>
                    <TabPanel value="2">
                        <p class="m-0">
                            <TipoCuotas :selectedCondominio="selectedItem" />
                        </p>
                    </TabPanel>
                    <TabPanel value="3">
                        <p class="m-0">
                            <ConceptosCA :selectedCondominio="selectedItem" />
                        </p>
                    </TabPanel>
                    <TabPanel value="4">
                        <p class="m-0">
                            <TipoEgresos :selectedCondominio="selectedItem" />
                        </p>
                    </TabPanel>
                    <TabPanel value="5">
                        <p class="m-0">
                            <Users :selectedCondominio="selectedItem" />
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
import 'primeicons/primeicons.css';
import Lotes from './Lotes.vue';
import TipoLotes from './TipoLotes.vue';
import TipoCuotas from './TipoCuotas.vue';
import Users from './Users.vue';
import ConceptosCA from './ConceptosCA.vue';
import TipoEgresos from './TipoEgresos.vue';


export default {
    components: {
        Tabs, TabList, Tab, TabPanels, TabPanel, Button, Select, Lotes, TipoLotes, TipoCuotas, Users, ConceptosCA, TipoEgresos
    },
    data() {
        return {
            authStore: useAuthStore(),
            selectedItem: null,
            condominiosList: [],
            userData: null,
            items: [
                { id: '0', label: 'Lotes', icon: 'pi pi-home' },
                { id: '1', label: 'Tipo de Lotes', icon: 'pi pi-tags' },
                { id: '2', label: 'Cuotas', icon: 'pi pi-dollar' },
                { id: '3', label: 'Tipo de Cargos / Abonos', icon: 'pi pi-dollar' },
                { id: '4', label: 'Tipo de Egresos', icon: 'pi pi-money-bill' },
                { id: '5', label: 'Usuarios', icon: 'pi pi-address-book' }
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
