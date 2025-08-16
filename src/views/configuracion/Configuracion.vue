<template>
    <div class="card">
        <div class="card">
            <h3><i class="pi pi-cog" style="font-size: 1rem" /> Configuracion</h3>
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
                        <Lotes />
                    </p>
                </TabPanel>
                <TabPanel value="1">
                    <p class="m-0">
                        <TipoLotes />
                    </p>
                </TabPanel>
                <TabPanel value="2">
                    <p class="m-0">
                        <TipoCuotas />
                    </p>
                </TabPanel>
                <TabPanel value="3">
                    <p class="m-0">
                        <Users />
                    </p>
                </TabPanel>
            </TabPanels>
        </Tabs>
    </div>
</template>

<script>
import { Tab, TabList, TabPanel, TabPanels, Tabs } from 'primevue';
import Lotes from './Lotes.vue';
import TipoLotes from './TipoLotes.vue';
import TipoCuotas from './TipoCuotas.vue';
import Users from './Users.vue';


export default {
    components: {
        Tabs, TabList, Tab, TabPanels, TabPanel, Lotes, TipoLotes, TipoCuotas, Users
    },
    data() {
        return {
            items: [
                { id: '0', label: 'Lotes', icon: 'pi pi-home' },
                { id: '1', label: 'Tipo de Lotes', icon: 'pi pi-tags' },
                { id: '2', label: 'Cuotas', icon: 'pi pi-dollar' },
                { id: '3', label: 'Usuarios', icon: 'pi pi-address-book' }
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
