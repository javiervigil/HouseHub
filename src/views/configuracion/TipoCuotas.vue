<template>
    <Toast />
    <ConfirmPopup></ConfirmPopup>
    <div class="leftcontent">
        <TableBar @addItem="addItem" @editItem="editItem" @deleteItem="deleteItem" @refreshTable="refreshTable" />
        <DataTable v-model:selection="selectedItems" :value="items" sortField="id" stripedRows size="small" paginator
            :rows="10" selectionMode="multiple" :metaKeySelection="metaKey" dataKey="id">
            <Column selectionMode="multiple" headerStyle="width: 3rem"></Column>
            <Column field="id" header="Id" hidden></Column>
            <Column field="name" header="Nombre" sortable></Column>
            <Column field="amount" header="Monto" sortable>
                <template #body="slotProps">
                    $ {{ slotProps.data.amount }}
                </template>
            </Column>
            <Column field="description" header="Descripcion" sortable></Column>

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
                    <InputText name="amount" type="number" placeholder="Monto" fluid />
                    <Message v-if="$form.amount?.invalid" severity="error" size="small" variant="simple">{{
                        $form.amount.error.message }}</Message>
                </div>
                <div class="flex flex-col gap-1">
                    <InputText name="description" type="text" placeholder="Descripcion" fluid />
                    <Message v-if="$form.description?.invalid" severity="error" size="small" variant="simple">{{
                        $form.description.error.message }}</Message>
                </div>

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
            metaKey: ref(true),
            items: ref([]),
            showAdd: ref(false),
            newItem: ref(false),
            initialValues: {
                id: 0,
                name: '',
                amount: 0,
                description: ''
            }
        }
    },
    methods: {
        // llamadas al api
        async refreshTable() {
            try {
                const response = await apiService.getTiposCuota();
                this.items = response.data;
            } catch (error) {
                console.error("Failed to fetch CuotaTypes:", error);
            }
        },
        async updateCuotaType() {
            try {
                return (await apiService.updateTipoCuota(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to update CuotaTypes:", error);
            }
        },
        async createCuotaType() {
            try {
                return (await apiService.createTipoCuota(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to create CuotaTypes:", error);
            }
        },
        async deleteCuotaType(id) {
            try {
                await apiService.deleteTipoCuota(id);
            } catch (error) {
                console.error("Failed to delete CuotaTypes:", error);
            }
        },


        // listeners de eventos de la barra de acciones
        addItem() {
            this.titulo = 'Agregar nueva cuota';
            this.initialValues.id = null;
            this.initialValues.name = '';
            this.initialValues.description = '';
            this.initialValues.amount = 0;
            this.showAdd = true;
            this.newItem = true;
        },
        editItem() {
            if (this.selectedItems.length != 1) {
                this.toast.add({ severity: 'error', summary: 'Select only one item to edit.', life: 3000 });
            } else {
                this.titulo = 'Actualizar cuota';
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
                            this.deleteCuotaType(item.id);
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
            if (!values.amount) {
                errors.amount = [{ message: 'Amount is required.' }];
            }
            if (!values.description) {
                errors.description = [{ message: 'Description is required.' }];
            }
            this.initialValues.name = values.name;
            this.initialValues.description = values.description;
            this.initialValues.amount = values.amount;
            return {
                errors
            };
        },
        async onFormSubmit({ valid }) {
            if (valid) {
                this.toast.add({ severity: 'success', summary: 'Tipo de cuota actualizado correctamente.', life: 3000 });
                this.showAdd = false;
                this.selectedItems = [];
                if (this.newItem) {
                    this.initialValues = await this.createCuotaType();
                    this.items = [...this.items, this.initialValues];
                    this.refreshTable();
                } else {
                    this.updateCuotaType();
                    this.items = this.items.map(item =>
                        item.id === this.initialValues.id ? {
                            ...item,
                            id: this.initialValues.id,
                            name: this.initialValues.name,
                            description: this.initialValues.description,
                            amount: this.initialValues.amount
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
.leftcontent {
    width: 50%;
}

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