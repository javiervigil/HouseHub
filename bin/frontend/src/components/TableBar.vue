<template>
    <div style="text-align: right; ">
        <Button  v-if="showIcons[0]"  icon="pi pi-plus" variant="text" v-tooltip.top="'Agregar'" @click="addItem" /><!-- add -->
        <Button v-if="showIcons[1]" icon="pi pi-pencil" variant="text" v-tooltip.top="'Modificar'" @click="editItem" /><!-- edit -->
        <Button v-if="showIcons[2]" icon="pi pi-trash" variant="text" v-tooltip.top="'Borrar'" @click="deleteItem" /><!-- delete -->
        <Button v-if="showIcons[3]" icon="pi pi-file-excel" variant="text" v-tooltip.top="'Exportar'"
            @click="exportExcel" /><!-- export excel -->
        <Button v-if="showIcons[4]" icon="pi pi-sync" variant="text" v-tooltip.top="'Actualizar'" @click="refreshTable" /><!-- refresh -->
    </div>

</template>

<script>
import 'primeicons/primeicons.css';
import { Button } from 'primevue';

export default {

    props: {
        showIcons: {
            type: Array,
            default: [1,1,1,1,1]
        },
    },

    components: {
        Button
    }, methods: {
        addItem(event) {
            this.$emit('addItem', event);
        },
        editItem(event) {
            this.$emit('editItem', event);
        },
        deleteItem(event) {
            this.$emit('deleteItem', event);
        },
        exportExcel(event) {
            this.$emit('exportExcel', event);
        },
        refreshTable(event) {
            this.$emit('refreshTable', event);
        }
    }
}
</script>

<style></style>