<template>
    <Toast />
    <ConfirmPopup></ConfirmPopup>
    <div style="width: 80%;">
        <TableBar :showIcons="[1, 0, 1, 1, 1]" @addItem="addItem" @deleteItem="deleteItem" @refreshTable="refreshTable"
            @exportExcel="exportExcel" />
        <DataTable v-model:selection="selectedItems" :value="items" sortField="id" stripedRows size="small" paginator
            :rows="10" selectionMode="multiple" :metaKeySelection="metaKey" dataKey="id">
            <template #empty> No existen datos para esta tabla. </template>
            <template #loading> Cargando informacion.... </template>
            <Column selectionMode="multiple" style="width: 5%;"></Column>
            <Column field="name" header="Nombre" style="width: 15%;"></Column>
            <Column field="email" header="Correo" style="width: 15%;"></Column>
            <Column field="phone" header="Telefono" style="width: 15%;"></Column>
            <Column field="adderss" header="Direccion" style="width: 15%;"></Column>
            <Column field="lotesNames" header="Lotes Asociados" style="width: 15%;"></Column>
            <Column header="Resetear Contrasena" style="width: 15%; text-align: center;">
                <template #body="{ data }">
                    <Button icon="pi pi-plus" variant="text" v-tooltip.top="'Resetear Contrasena'"
                        @click="resetPassword(data.id)">
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
                <div class="flex flex-col gap-1">
                    <InputText name="address" type="text" placeholder="Direccion" fluid />
                    <Message v-if="$form.address?.invalid" severity="error" size="small" variant="simple">{{
                        $form.address.error.message }}</Message>
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
            selectedItems: ref([]),
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
                address: '',
                lote: null
            }
        }
    },
    methods: {
        // llamadas al api
        async refreshTable() {
            try {
                if (this.selectedCondominio) {
                    this.items = (await apiService.getUsuariosByCondominio(this.selectedCondominio.id)).data;
                }
            } catch (error) {
                console.error("Failed to fetch Usuarios:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },
        async getLotes() {
            try {
                this.itemsLote = (await apiService.getLotesByCondominio(this.selectedCondominio.id)).data;
            } catch (error) {
                console.error("Failed to fetch Lotes:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },
        async updateUsuario() {
            try {
                return (await apiService.updateUsuario(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to update Usuario:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },
        async createUsuario() {
            try {
                return (await apiService.createUsuario(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to create Usuario:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
            }
        },
        async deleteUsuario(id) {
            try {
                await apiService.deleteUsuario(id);
                this.toast.add({ severity: 'info', summary: 'Registros borrados', life: 3000 });
                this.selectedItems = null;
                await this.refreshTable();
            } catch (error) {
                console.error("Failed to delete Usuario:", error);
                this.toast.add({ severity: 'error', summary: 'No se puede borrar el dato, verifique que no tenga dependencias con otros datos.', life: 3000 });
            }
        },

        resetPassword(id) {
            console.log('reste passsword id usuario ' + id);
        },

        // listeners de eventos de la barra de acciones
        addItem() {
            if (!this.selectedCondominio) {
                this.toast.add({ severity: 'error', summary: 'Seleccione un condominio para agregar un usuario.', life: 3000 });
                return;
            }
            this.titulo = 'Agregar nuevo usuario';
            this.getLotes();
            this.initialValues.id = null;
            this.initialValues.name = '';
            this.initialValues.email = '';
            this.initialValues.phone = '';
            this.initialValues.address = '';
            this.initialValues.lote = null;
            this.showAdd = true;
            this.newItem = true;
        },

        deleteItem(event) {
            if (!this.selectedItems || this.selectedItems.length === 0) {
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
                        for (const item of this.selectedItems) {
                            this.deleteUsuario(item.id);
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
                errors.name = [{ message: 'El nombre es requerido.' }];
            } else if (values.name.length > 40) {
                errors.name = [{ message: 'El nombre no puede tener mas de 40 caracteres.' }];
            }
            if (!values.email) {
                errors.email = [{ message: 'El email es requerido.' }];
            } else if (values.email.length > 60) {
                errors.email = [{ message: 'El email no puede tener mas de 60 caracteres.' }];
            }
            if (values.phone.length != 10) {
                errors.phone = [{ message: 'El telefono debe de sr de 10 digitos.' }];
            }
            if (values.address.length != 100) {
                errors.address = [{ message: 'La direccion no puede ser mayor a 100 caracteres.' }];
            }
            if (!values.lote) {
                errors.lote = [{ message: 'El lote es requerido.' }];
            }
            this.initialValues.name = values.name;
            this.initialValues.email = values.email;
            this.initialValues.phone = values.phone;
            this.initialValues.address = values.address;
            this.initialValues.lote = values.lote;
            return {
                errors
            };
        },
        async onFormSubmit({ valid }) {
            if (valid) {
                this.showAdd = false;
                this.selectedItems = [];
                if (this.newItem) {
                    this.initialValues = await this.createUsuario();
                    this.items = [...this.items, this.initialValues];
                    this.refreshTable();
                    this.toast.add({ severity: 'success', summary: 'Contacto actualizado correctamente.', life: 3000 });
                }
            }
        },
        //validacion del editor inline
        async onCellEditComplete(event) {
            let { data, field, newValue, originalValue } = event;
            if (!newValue) {
                newValue = data[field];
            } else {
                data[field] = newValue;
                this.initialValues = data;
                this.initialValues.condominio = this.selectedCondominio;
                await this.updateUsuario();
                await this.refreshTable();
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
        exportExcel() {
            if (this.items.length === 0) {
                this.toast.add({ severity: 'warn', summary: 'No hay datos para exportar.', life: 3000 });
            } else {
                //arregla titulo de columnas datos
                const itemsExport = this.items.map(item => {
                    return {
                        Nombre: item.name,
                        Email: item.email,
                        Telefono: item.phone,
                        Direccion: item.adderss,
                        Lote: item.lote
                    };
                });
                const worksheet = XLSX.utils.json_to_sheet(itemsExport);
                const workbook = XLSX.utils.book_new();
                XLSX.utils.book_append_sheet(workbook, worksheet, 'usuarios');

                // Genera el archivo binario
                const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
                const data = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8' });

                // Descarga el archivo
                saveAs(data, 'Listado de usuarios.xlsx');
            }
        }
    },
    watch: {
        selectedCondominio(newValue, oldValue) {
            this.refreshTable();
            this.selectedItems = null;
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