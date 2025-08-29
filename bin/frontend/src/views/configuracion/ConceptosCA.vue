<template>
    <Toast />
    <ConfirmPopup></ConfirmPopup>
    <div style="width: 70%; ">
        <div style="width: 100%;">
            <b>Conceptos para cargos</b>
            <TableBar :showIcons="[1, 0, 1, 1, 1]" @addItem="addItemC" @deleteItem="deleteItemC"
                @refreshTable="refreshTables" @exportExcel="exportExcel" />

            <DataTable v-model:selection="selectedItemsC" :value="itemsC" sortField="id" stripedRows size="small"
                columnResizeMode="expand" paginator :rows="10" selectionMode="multiple" :metaKeySelection="metaKeyC"
                dataKey="id" editMode="cell" @cell-edit-complete="onCellEditCompleteC" :pt="{
                    table: { style: 'min-width: 50rem' },
                    column: {
                        bodycell: ({ state }) => ({
                            class: [{ '!py-0': state['d_editing'] }]
                        })
                    }
                }">
                <template #empty> No existen datos para esta tabla. </template>
                <template #loading> Cargando informacion.... </template>
                <Column selectionMode="multiple" headerStyle="width: 2%"></Column>
                <Column field="name" header="Nombre" headerStyle="width: 48%">
                    <template #body="{ data }">
                        {{ data.name }}
                    </template>
                    <template #editor="{ data, field }">
                        <InputText v-model="data.name" autofocus fluid size="small"
                            @keydown.enter="$event.target.blur()" :class="{ 'p-invalid': !isNameValid(data, field) }" />
                        <Message v-if="!isNameValid(data, field)" severity="error" size="small" variant="simple">{{
                            errors.name }}
                        </Message>
                    </template>

                </Column>
                <Column field="description" header="Descripcion" headerStyle="width: 50%">
                    <template #body="{ data }">
                        {{ data.description }}
                    </template>
                    <template #editor="{ data, field }">
                        <InputText v-model="data.description" autofocus fluid size="small"
                            @keydown.enter="$event.target.blur()"
                            :class="{ 'p-invalid': !isDescriptionValid(data, field) }" />
                        <Message v-if="!isDescriptionValid(data, field)" severity="error" size="small" variant="simple">
                            {{
                                errors.description }}
                        </Message>
                    </template>
                </Column>
            </DataTable>
        </div>
        <br />
        <hr />
        <div style="width: 100%;">
            <b>Conceptos para abonos</b>
            <TableBar :showIcons="[1, 0, 1, 0, 0]" @addItem="addItemA" @deleteItem="deleteItemA" />

            <DataTable v-model:selection="selectedItemsA" :value="itemsA" sortField="id" stripedRows size="small"
                paginator :rows="10" selectionMode="multiple" :metaKeySelection="metaKeyA" dataKey="id" editMode="cell"
                @cell-edit-complete="onCellEditCompleteA" :pt="{
                    table: { style: 'min-width: 50rem' },
                    column: {
                        bodycell: ({ state }) => ({
                            class: [{ '!py-0': state['d_editing'] }]
                        })
                    }
                }">
                <template #empty> No existen datos para esta tabla. </template>
                <template #loading> Cargando informacion.... </template>
                <Column selectionMode="multiple" style="width: 2%"></Column>
                <Column field="name" header="Nombre" style="width: 30%">
                    <template #body="{ data }">
                        {{ data.name }}
                    </template>
                    <template #editor="{ data, field }">
                        <InputText v-model="data.name" autofocus fluid size="small"
                            @keydown.enter="$event.target.blur()" :class="{ 'p-invalid': !isNameValid(data, field) }" />
                        <Message v-if="!isNameValid(data, field)" severity="error" size="small" variant="simple">{{
                            errors.name }}
                        </Message>
                    </template>
                </Column>
                <Column field="description" header="Descripcion" style="width: 30%">
                    <template #body="{ data }">
                        {{ data.description }}
                    </template>
                    <template #editor="{ data, field }">
                        <InputText v-model="data.description" autofocus fluid size="small"
                            @keydown.enter="$event.target.blur()"
                            :class="{ 'p-invalid': !isDescriptionValid(data, field) }" />
                        <Message v-if="!isDescriptionValid(data, field)" severity="error" size="small" variant="simple">
                            {{
                                errors.description }}
                        </Message>
                    </template>
                </Column>
            </DataTable>
        </div>

    </div>


    <!-- Add  -->
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

import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';

export default {
    props: {
        selectedCondominio: {
            type: Object,
            default: null
        },
    },
    components: {
        DataTable, Column, TableBar, Dialog, InputText, Button, Form, Select, Message, ConfirmPopup, Toast
    },
    data() {
        return {
            titulo: ref(''),
            confirm: useConfirm(),
            toast: useToast(),
            itemsC: ref([]),
            itemsA: ref([]),
            metaKeyC: ref(true),
            metaKeyA: ref(true),
            selectedItemsC: ref([]),
            selectedItemsA: ref([]),

            showAdd: ref(false),
            newItem: ref(false),
            isC: null,


            initialValues: {
                id: 0,
                name: '',
                description: '',
                tipo: ''
            },
            errors: {
                name: '',
                description: ''
            }
        }
    },
    methods: {
        // llamadas al api
        async refreshTables() {
            await this.refreshTableC();
            await this.refreshTableA();
        },

        async refreshTableC() {
            try {
                if (this.selectedCondominio) {
                    this.itemsC = (await apiService.getTransaccionesLoteTypesByCondominio(this.selectedCondominio.id, 2)).data;
                }
            } catch (error) {
                console.error("Failed to fetch CuotaTypes:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },
        async refreshTableA() {
            try {
                if (this.selectedCondominio) {
                    this.itemsA = (await apiService.getTransaccionesLoteTypesByCondominio(this.selectedCondominio.id, 1)).data;
                }
            } catch (error) {
                console.error("Failed to fetch CuotaTypes:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },

        async updateTransaccionesLoteTypes() {
            try {
                return (await apiService.updateTransaccionesLoteTypes(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to update CuotaTypes:", error);
                this.toast.add({ severity: 'error', summary: 'Error al actualizar el registro.', life: 3000 });
            }
        },
        async createTransaccionesLoteTypes() {
            try {
                return (await apiService.createTransaccionesLoteTypes(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to create CuotaTypes:", error);
                this.toast.add({ severity: 'error', summary: 'Error al crear un nuevo registro.', life: 3000 });
            }
        },
        async deleteTransaccionesLoteTypes(id) {
            try {
                await apiService.deleteTransaccionesLoteTypes(id);
                this.toast.add({ severity: 'info', summary: 'Registros borrados', life: 3000 });
                this.selectedItems = null;
                await this.refreshTable();
            } catch (error) {
                console.error("Failed to delete CuotaTypes:", error);
                this.toast.add({ severity: 'error', summary: 'No se puede borrar el dato, verifique que no tenga dependencias con otros datos.', life: 3000 });
            }
        },
        formatCurrency(value) {
            return '$ ' + value.toLocaleString('es-MX', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            });
        },

        //   ******************** todo para CARGOS
        // listeners de eventos de la barra de acciones
        addItemC() {
            if (!this.selectedCondominio) {
                this.toast.add({ severity: 'error', summary: 'Seleccione un condominio para agregar un concepto.', life: 3000 });
                return;
            }
            this.titulo = 'Agregar nuevo tipo de cargo';
            this.initialValues.id = null;
            this.initialValues.name = '';
            this.initialValues.description = '';
            this.showAdd = true;
            this.newItem = true;
            this.isC = true;
        },
        deleteItemC(event) {
            if (!this.selectedItemsC || this.selectedItemsC.length === 0) {
                this.toast.add({ severity: 'error', summary: 'Seleccione un elemento para borrar.', life: 3000 });
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
                        for (const item of this.selectedItemsC) {
                            this.deleteTransaccionesLoteTypes(item.id);
                            this.itemsC = this.itemsC.filter(itemx => itemx.id !== item.id);
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
                errors.name = [{ message: 'EL campo nombre es requerido.' }];
            } else if (values.name.length > 40) {
                errors.name = [{ message: 'El nombre no puede tener mas de 40 caracteres.' }];
            }
            if (!values.description) {
                errors.description = [{ message: 'La descripcion es requerida.' }];
            } else if (values.description.length > 60) {
                errors.description = [{ message: 'La descripcion no puede tener mas de 60 caracteres.' }];
            }
            this.initialValues.name = values.name;
            this.initialValues.description = values.description;
            return {
                errors
            };
        },
        async onFormSubmit({ valid }) {
            if (valid) {
                this.showAdd = false;
                this.selectedItems = [];
                if (this.newItem) {
                    this.initialValues.condominio = this.selectedCondominio;
                    if (this.isC) { // CARGO = 2
                        this.initialValues.tipo = "CARGO";
                    } else { // ABONO = 1
                        this.initialValues.tipo = "ABONO";
                    }
                    this.initialValues = await this.createTransaccionesLoteTypes();
                    //this.items = [...this.items, this.initialValues];
                    this.refreshTables();
                    this.toast.add({ severity: 'success', summary: 'Tipo de cargo / abono agregado correctamente.', life: 3000 });
                }
            }
        },

        isNameValid(data, field) {
            if (!data[field]) {
                this.errors.name = 'El nombre es un campo requerido.';
                return false;
            } else if (data[field].length > 40) {
                this.errors.name = 'El nombre no puede tener mas de 40 caracteres.';
                return false;
            }
            return true;
        },
        isDescriptionValid(data, field) {
            if (!data[field]) {
                this.errors.description = 'La descripcion es un campo requerido.';
                return false;
            } else if (data[field].length > 60) {
                this.errors.description = 'La descripcion no puede tener mas de 60 caracteres.';
                return false;
            }
            return true;
        },

        //validacion del editor inline
        async onCellEditCompleteC(event) {
            let { data, field, newValue, originalValue } = event;
            if (!newValue) {
                newValue = data[field];
            } else {
                data[field] = newValue;
                this.initialValues = data;
                this.initialValues.condominio = this.selectedCondominio;
                await this.updateTransaccionesLoteTypes();
                await this.refreshTables();
            }
        },

        exportExcel() {
            if (this.itemsC.length === 0 && this.itemsA.length === 0) {
                this.toast.add({ severity: 'warn', summary: 'No hay datos para exportar.', life: 3000 });
            } else {
                //arregla titulo de columnas datos
                let itemsCargos = this.itemsC.map(item => {
                    return {
                        Nombre: item.name,
                        'Cargo/Abono': 'Cargo',
                        Descripcion: item.description
                    };
                });
                let itemsAbonos = this.itemsA.map(item => {
                    return {
                        Nombre: item.name,
                        'Cargo/Abono': 'Abono',
                        Descripcion: item.description
                    };
                });
                const itemsExport = [...itemsCargos, ...itemsAbonos];
                const worksheet = XLSX.utils.json_to_sheet(itemsExport);
                const workbook = XLSX.utils.book_new();
                XLSX.utils.book_append_sheet(workbook, worksheet, 'Cargos_Abonos');

                // Genera el archivo binario
                const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
                const data = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8' });

                // Descarga el archivo
                saveAs(data, 'Tipos de Cargos-Abonos.xlsx');
            }
        },



        //   ******************** todo para CARGOS
        // listeners de eventos de la barra de acciones
        addItemA() {
            if (!this.selectedCondominio) {
                this.toast.add({ severity: 'error', summary: 'Seleccione un condominio para agregar un concepto.', life: 3000 });
                return;
            }
            this.titulo = 'Agregar nuevo tipo de abono';
            this.initialValues.id = null;
            this.initialValues.name = '';
            this.initialValues.description = '';
            this.showAdd = true;
            this.newItem = true;
            this.isC = false;
        },
        deleteItemA(event) {
            if (!this.selectedItemsA || this.selectedItemsA.length === 0) {
                this.toast.add({ severity: 'error', summary: 'Seleccione un elemento para borrar.', life: 3000 });
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
                        for (const item of this.selectedItemsA) {
                            this.deleteTransaccionesLoteTypes(item.id);
                            this.itemsA = this.itemsA.filter(itemx => itemx.id !== item.id);
                        }
                    },
                    reject: () => {
                    }
                });
            }
        },

        //validacion del editor inline
        async onCellEditCompleteA(event) {
            let { data, field, newValue, originalValue } = event;
            if (!newValue) {
                newValue = data[field];
            } else {
                data[field] = newValue;
                this.initialValues = data;
                this.initialValues.condominio = this.selectedCondominio;
                await this.updateTransaccionesLoteTypes();
                await this.refreshTables();
            }
        },
    },
    watch: {
        selectedCondominio(newValue, oldValue) {
            this.refreshTables();
            this.selectedItemsC = null;
            this.selectedItemsA = null;
        }
    },
    mounted() {
        this.refreshTables();
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