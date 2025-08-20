<template>
    <div class="card">
        <div class="card">
            <h3><i class="pi pi-cog" style="font-size: 1rem" /> Administracion</h3>
        </div>
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
                        <Transacciones />
                    </p>
                </TabPanel>
                <TabPanel value="1">
                    <p class="m-0">
                        <EstadoCuenta />
                    </p>
                </TabPanel>
                <TabPanel value="2">
                    <p class="m-0">
                        <TipoCuotas />
                    </p>
                </TabPanel>
                <TabPanel value="3">
                    <p class="m-0">
                        <BitacoraAccesos />
                    </p>
                </TabPanel>
            </TabPanels>
        </Tabs>
    </div>
</template>

<script>
import { Tab, TabList, TabPanel, TabPanels, Tabs } from 'primevue';
import Transacciones from './Transacciones.vue';
import EstadoCuenta from './EstadoCuenta.vue';
import BitacoraAccesos from './BitacoraAccesos.vue';



export default {
    components: {
        Tabs, TabList, Tab, TabPanels, TabPanel, Transacciones, EstadoCuenta, BitacoraAccesos

    },
    data() {
        return {
            items: [
                { id: '0', label: 'Transacciones por lote', icon: 'pi pi-arrow-right-arrow-left' },
                { id: '1', label: 'Estados de Cuenta', icon: 'pi pi-calculator' },
                { id: '2', label: 'Cuotas', icon: 'pi pi-file-export' },
                { id: '3', label: 'Bitacora Acceso', icon: 'pi pi-address-book' }
            ]
        }
    },

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
