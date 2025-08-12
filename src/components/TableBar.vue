<template>
    <div style="text-align: right; ">
        <Button icon="pi pi-plus" variant="text" v-tooltip.top="'Agregar'"   @click="addItem" /><!-- add -->
        <Button icon="pi pi-pencil" variant="text" v-tooltip.top="'Modificar'"  @click="editItem" /><!-- edit -->
        <Button icon="pi pi-trash" variant="text" v-tooltip.top="'Borrar'"  @click="deleteItem" /><!-- delete -->
        <Button icon="pi pi-sync" variant="text" v-tooltip.top="'Actualizar'"  @click="refreshTable" /><!-- refresh -->
    </div>
</template>

<script>
import 'primeicons/primeicons.css';
import { Button } from 'primevue';

export default {
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
        refreshTable(event) {
            this.$emit('refreshTable', event);
        }
    }
}
</script>

<style></style>