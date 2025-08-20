<template>
    <Toast />
    <ConfirmPopup></ConfirmPopup>
    <div style="width: 50%;">
        <TableBar @addItem="addItem" @editItem="editItem" @deleteItem="deleteItem" @refreshTable="refreshTable" />
        <DataTable v-model:selection="selectedItems" :value="items" sortField="id" stripedRows size="small" paginator
            :rows="10" selectionMode="multiple" :metaKeySelection="metaKey" dataKey="id">
            <Column selectionMode="multiple" headerStyle="width: 3rem"></Column>
            <Column field="id" header="Id" hidden></Column>
            <Column field="name" header="Nombre" sortable></Column>
            <Column field="email" header="Correo" sortable></Column>
            <Column field="phone" header="Telefono" sortable></Column>
            <Column field="adderss" header="Direccion" sortable></Column>
            <Column field="lotesNames" header="Lotes Asociados" sortable></Column>
            <Column header="Resetear Contrasena">
                <template #body="">
                    <Button icon="pi pi-plus" variant="text" v-tooltip.top="'Resetear Contrasena'"
                        @click="resetPassword">
                    </Button>
                </template>
            </Column>
        </DataTable>
    </div>


    <!-- Add / Edit item -->
    <Dialog v-model:visible="showAdd" modal :header="titulo" :style="{ width: '25rem' }">
        <div class="form">
            <Form v-slot="$form" v-bind:initialValues="initialValues" v-bind:resolver="resolver"
                :validateOnValueUpdate="false" :validateOnBlur="false" @submit="onFormSubmit"
                class="flex flex-col gap-4 w-full sm:w-56">
                <div class="flex flex-col gap-1">
                    <InputText name="name" type="text" placeholder="Nombre" fluid />
                    <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">{{
                        $form.name.error.message }}</Message>
                </div>
                <div class="flex flex-col gap-1">
                    <InputText name="email" type="text" placeholder="Correo" fluid />
                    <Message v-if="$form.email?.invalid" severity="error" size="small" variant="simple">{{
                        $form.email.error.message }}</Message>
                </div>
                <div class="flex flex-col gap-1">
                    <InputText name="phone" type="text" placeholder="Telefono" fluid />
                    <Message v-if="$form.phone?.invalid" severity="error" size="small" variant="simple">{{
                        $form.phone.error.message }}</Message>
                </div>
                <Select name="lote" :options="itemsLote" optionLabel="name" placeholder="Lote" fluid
                    style=" margin-bottom: 10px;    margin-top: 10px;">
                </Select>
                <Message v-if="$form.lote?.invalid" severity="error" size="small" variant="simple">{{
                    $form.lote.error.message }}</Message>


                <div class="formButtons">
                    <Button type="button" label="Cancelar" severity="secondary" @click="showAdd = false"
                        style="margin-top: 20px;"></Button>
                    <Button type="submit" label="Guardar" style="margin-left: 20px;"></Button>
                </div>
            </Form>
        </div>
    </Dialog>
</template>

<script>

import apiService from '@/service/apiService';
import { ref } from 'vue';
import TableBar from '@/components/TableBar.vue';
import { Button, Column, ConfirmPopup, DataTable, Dialog, InputText, Message, Select, Toast } from 'primevue';
import { Form } from '@primevue/forms';
import { useToast } from 'primevue/usetoast';
import { useConfirm } from "primevue/useconfirm";

export default {
    components: {
        DataTable, Column, TableBar, Dialog, InputText, Button, Form, Select, Message, ConfirmPopup, Toast
    },
    data() {
        return {
            titulo: ref(''),
            confirm: useConfirm(),
            toast: useToast(),
            selectedItems: ref([]),
            selectedLote: ref(),
            metaKey: ref(true),
            items: ref([]),
            itemsLote: ref([]),
            showAdd: ref(false),
            newItem: ref(false),
            initialValues: {
                id: 0,
                name: '',
                email: '',
                phone: '',
                lote: 0
            }
        }
    },
    methods: {
        // llamadas al api
        async refreshTable() {
            try {
                const response = await apiService.getUsuariosByRole(1);
                this.items = response.data;
            } catch (error) {
                console.error("Failed to fetch Usuarios:", error);
            }
        },
        async updateUsuario() {
            try {
                return (await apiService.updateUsuario(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to update Usuario:", error);
            }
        },
        async createUsuario() {
            try {
                return (await apiService.createUsuario(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to create Usuario:", error);
            }
        },
        async deleteUsuario(id) {
            try {
                await apiService.deleteUsuario(id);
            } catch (error) {
                console.error("Failed to delete Usuario:", error);
            }
        },


        // listeners de eventos de la barra de acciones
        addItem() {
            this.titulo = 'Agregar nuevo contacto';
            this.getLotes();
            this.initialValues.id = null;
            this.initialValues.name = '';
            this.initialValues.email = '';
            this.initialValues.phone = '';
            this.initialValues.lote = null;
            this.showAdd = true;
            this.newItem = true;
        },
        editItem() {
            if (this.selectedItems.length != 1) {
                this.toast.add({ severity: 'error', summary: 'Select only one item to edit.', life: 3000 });
            } else {
                this.titulo = 'Actualizar contacto';
                this.getLotes();
                this.initialValues = this.selectedItems[0];
                this.showAdd = true;
                this.newItem = false;
            }
        },
        deleteItem(event) {
            if (this.selectedItems.length === 0) {
                this.toast.add({ severity: 'error', summary: 'Select at least one item to delete.', life: 3000 });
            } else {
                this.confirm.require({
                    target: event.currentTarget,
                    message: 'Desea borrar el/los registros?',
                    icon: 'pi pi-info-circle',
                    rejectProps: {
                        label: 'Cancelar',
                        severity: 'secondary',
                        outlined: true
                    },
                    acceptProps: {
                        label: 'Borrar',
                        severity: 'danger'
                    },
                    accept: () => {
                        this.toast.add({ severity: 'info', summary: 'Registros borrados', life: 3000 });
                        for (const item of this.selectedItems) {
                            this.deleteContacto(item.id);
                            this.items = this.items.filter(itemx => itemx.id !== item.id);
                        }
                    },
                    reject: () => {
                    }
                });
            }
        },

        // Acciones del Form 
        resolver({ values }) {
            const errors = {};
            if (!values.name) {
                errors.name = [{ message: 'Name is required.' }];
            }
            if (!values.email) {
                errors.email = [{ message: 'Description is required.' }];
            }
            if (!values.phone) {
                errors.phone = [{ message: 'Phone is required.' }];
            }
            if (!values.lote) {
                errors.lote = [{ message: 'Lote is required.' }];
            }
            this.initialValues.name = values.name;
            this.initialValues.email = values.email;
            this.initialValues.phone = values.phone;
            this.initialValues.lote = values.lote;
            return {
                errors
            };
        },
        async onFormSubmit({ valid }) {
            if (valid) {
                this.toast.add({ severity: 'success', summary: 'Contacto actualizado correctamente.', life: 3000 });
                this.showAdd = false;
                this.selectedItems = [];
                if (this.newItem) {
                    this.initialValues = await this.createContacto();
                    this.items = [...this.items, this.initialValues];
                    this.refreshTable();
                } else {
                    this.updateContacto();
                    this.items = this.items.map(item =>
                        item.id === this.initialValues.id ? {
                            ...item,
                            id: this.initialValues.id,
                            name: this.initialValues.name,
                            email: this.initialValues.email,
                            phone: this.initialValues.phone,
                            lote: this.initialValues.lote
                        } : item
                    );
                }
            }
        }
    },
    mounted() {
        this.refreshTable();
    }
}

</script>

<style scope>
input,
select {
    margin-bottom: 10px;
    margin-top: 10px;
}

.formButtons {
    align-items: center;
    align-content: center;
}
</style>