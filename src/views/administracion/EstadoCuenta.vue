<template>
    <Toast />
    <ConfirmPopup></ConfirmPopup>

    <h3>Estado de Cuenta del Condominio</h3>
    <div style="display: flex; justify-content: left;">
        <div style="width: 40%; padding-right: 30px;">
            <TableBar @addItem="addItem" @editItem="editItem" @deleteItem="deleteItem" @refreshTable="refreshTable" />
            <DataTable v-model:selection="selectedItems" :value="items" sortField="date" stripedRows size="small"
                :metaKeySelection="metaKey" dataKey="id" :sortOrder="1" selectionMode="multiple">
                <Column selectionMode="multiple" headerStyle="width: 3rem"></Column>
                <Column field="loteId" header="Id" hidden></Column>
                <Column field="name" header="Lote"></Column>
                <Column field="totalCargos" header="Cargos">
                    <template #body="slotProps">
                        <div class="cargo">
                            {{ formatCurrency(slotProps.data.totalCargos) }}
                        </div>
                    </template>
                </Column>
                <Column field="totalAbonos" header="Abonos">
                    <template #body="slotProps">
                        <div>
                            {{ formatCurrency(slotProps.data.totalAbonos) }}
                        </div>
                    </template>
                </Column>
                <ColumnGroup type="footer">
                    <Row>
                        <Column footer="Totales:" :colspan="2" footerStyle="text-align:right" />
                        <Column>
                            <template #footer="slotProps">
                                <div class="cargo"><b>{{ formatCurrency(cargoTotal) }}</b></div>
                            </template>
                        </Column>
                        <Column :footer="formatCurrency(abonoTotal)" />
                    </Row>
                    <Row>
                        <Column footer="Diferencia:" :colspan="2" footerStyle="text-align:right" />
                        <Column>
                            <template #footer="slotProps">
                                <div v-if="abonoTotal - cargoTotal < 0" class="cargo"><b>{{ formatCurrency(abonoTotal -
                                    cargoTotal) }} (saldo en contra)</b></div>
                            </template>
                        </Column>
                        <Column>
                            <template #footer="slotProps">
                                <div v-if="abonoTotal - cargoTotal > 0"><b>{{ formatCurrency(abonoTotal - cargoTotal) }}
                                        (saldo a favor)</b></div>
                            </template>
                        </Column>

                    </Row>
                </ColumnGroup>

            </DataTable>

        </div>
        <div class="chart-right" style="width: 30%;">
            <Chart type="bar" :data="chartData" />
        </div>
    </div>

    <!-- Add / Edit item -->
    <Dialog v-model:visible="showAdd" modal :header="titulo" :style="{ width: '25rem' }">
        <div class="form">
            <Form v-slot="$form" v-bind:initialValues="initialValues" v-bind:resolver="resolver"
                :validateOnValueUpdate="false" :validateOnBlur="false" @submit="onFormSubmit"
                class="flex flex-col gap-4 w-full sm:w-56">
                <div class="flex flex-col gap-1">
                    <Select name="tipo" :options="itemsCA" optionLabel="name" placeholder="Tipo" fluid
                        style=" margin-bottom: 10px;  margin-top: 20px;" v-model="selectedType">
                    </Select>
                    <Message v-if="$form.tipo?.invalid" severity="error" size="small" variant="simple">{{
                        $form.tipo.error.message }}</Message>
                </div>
                <div class="flex flex-col gap-1">
                    <DatePicker name="date" showIcon fluid iconDisplay="input" dateFormat="dd/mm/yy" />
                    <Message v-if="$form.date?.invalid" severity="error" size="small" variant="simple">{{
                        $form.date.error.message }}</Message>
                </div>
                <div class="flex flex-col gap-1">
                    <InputText name="concept" type="text" placeholder="Concepto" fluid />
                    <Message v-if="$form.concept?.invalid" severity="error" size="small" variant="simple">{{
                        $form.concept.error.message }}</Message>
                </div>

                <div class="flex flex-col gap-1">
                    <InputText name="ammount" type="number" placeholder="Monto" fluid />
                    <Message v-if="$form.ammount?.invalid" severity="error" size="small" variant="simple">{{
                        $form.ammount.error.message }}</Message>
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
import { Button, Column, ColumnGroup, ConfirmPopup, DataTable, DatePicker, Dialog, InputText, Message, Row, Select, Toast } from 'primevue';
import { Form } from '@primevue/forms';
import { useToast } from 'primevue/usetoast';
import { useConfirm } from "primevue/useconfirm";
import Chart from 'primevue/chart';
import ChartDataLabels from 'chartjs-plugin-datalabels';



export default {

    components: {
        DataTable, Column, ColumnGroup, Row, TableBar, Dialog, InputText, Button, Form, Select, DatePicker, Message, ConfirmPopup, Toast, Chart
    },
    data() {
        return {
            plugins: [ChartDataLabels],
            lotes: ref([]),

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
                tipo: 0,
                name: '',
                date: null,
                ammount: 0,
                concept: ''
            },
            chartData: null,
            chartOptions: null
        }
    },
    methods: {
        // llamadas al api
        async refreshTable() {
            try {
                const response = await apiService.getTransaccionesLoteTotales();
                this.items = response.data;

                this.chartData = this.setChartData();
                this.chartOptions = this.setChartOptions();
            } catch (error) {
                console.error("Failed to fetch TransaccionesLote:", error);
            }
        },

        async updateTransaccionesLote() {
            try {
                return (await apiService.updateTransaccionesLote(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to update TransaccionesLote:", error);
            }
        },
        async createTransaccionesLote() {
            try {
                return (await apiService.createTransaccionesLote(this.initialValues)).data;
            } catch (error) {
                console.error("Failed to create TransaccionesLote:", error);
            }
        },
        async deleteTransaccionesLote(id) {
            try {
                await apiService.deleteTransaccionesLote(id);
            } catch (error) {
                console.error("Failed to delete TransaccionesLote:", error);
            }
        },
        async getAbonosList(id) {
            try {
                this.itemsCA = (await apiService.getTransaccionesLoteTypesByType(1)).data;

            } catch (error) {
                console.error("Failed to delete TransaccionesLote:", error);
            }
        },
        async getCargosList(id) {
            try {
                this.itemsCA = (await apiService.getTransaccionesLoteTypesByType(2)).data;
            } catch (error) {
                console.error("Failed to delete TransaccionesLote:", error);
            }
        },

        formatDate(value) {
            const dateValue = new Date(value);
            return dateValue.toLocaleDateString('es-MX', {
                day: '2-digit',
                month: 'short',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });
        },
        formatDateShort(value) {
            const dateValue = new Date(value);
            return dateValue.toLocaleDateString('es-MX', {
                day: '2-digit',
                month: 'short',
                year: 'numeric'

            });
        },
        formatCurrency(value) {
            return '$ ' + value.toLocaleString('es-MX', {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            });
        },


        // listeners de eventos de la barra de acciones
        async addAbono() {
            if (this.selectedLote) {
                this.titulo = 'Agregar abono';
                this.initialValues.id = null;
                this.initialValues.tipo = 1;
                this.initialValues.concept = '';
                this.initialValues.amount = '';
                this.initialValues.date = this.formatDateShort(new Date());
                this.initialValues.type = 'ABONO';

                this.selectedType = null;
                this.showAdd = true;
                this.newItem = true;
            } else {
                this.toast.add({ severity: 'error', summary: 'Select one item to add a payment.', life: 3000 });
            }
        },
        async addCargo() {
            if (this.selectedLote) {
                this.titulo = 'Agregar cargo';
                this.initialValues.id = null;
                this.initialValues.tipo = 2;
                this.initialValues.concept = '';
                this.initialValues.amount = '';
                this.initialValues.date = this.formatDateShort(new Date());
                this.initialValues.type = 'CARGO';

                this.selectedType = null;
                this.showAdd = true;
                this.newItem = true;
            } else {
                this.toast.add({ severity: 'error', summary: 'Select one item to add a charge.', life: 3000 });
            }
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
            if (!values.tipo) {
                errors.tipo = [{ message: 'Type is required.' }];
            }
            if (!values.concept) {
                errors.concept = [{ message: 'Concept is required.' }];
            }

            console.log('values.date ' + values.date);
            if (!values.date) {
                errors.date = [{ message: 'Date is required.' }];
            }
            if (!values.ammount) {
                errors.ammount = [{ message: 'Amount is required.' }];
            }
            if (values.ammount <= 0) {
                errors.ammount = [{ message: 'Amount value invalid.' }];
            }

            this.initialValues.name = values.name;
            this.initialValues.concept = values.concept;
            this.initialValues.ammount = values.ammount;

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
        },

        setChartData() {
            return {
                labels: ['Cargos', 'Aboos'],
                datasets: [
                    {
                        data: [this.cargoTotal, this.abonoTotal],
                        backgroundColor: ['red', 'lightgreen']
                    }
                ]
            };
        },
        setChartOptions() {
            return {
                datalabels: {
                    formatter: (value) => {
                        return new Intl.NumberFormat('es-MX', {
                            style: 'currency',
                            currency: 'MXN'
                        }).format(value);
                    },
                    color: '#fff',
                    font: {
                        weight: 'bold'
                    }
                },
                tooltip: {
                    callbacks: {
                        label: (context) => {
                            let label = context.dataset.label || '';
                            if (label) {
                                label += ': ';
                            }
                            if (context.parsed.y !== null) {
                                label += new Intl.NumberFormat('es-MX', {
                                    style: 'currency',
                                    currency: 'MXN'
                                }).format(context.parsed.y);
                            }
                            return label;
                        }
                    }
                }
            }
        },
    },
    mounted() {
        this.refreshTable();
    },
    computed: {
        cargoTotal() {
            let total = 0;
            for (let item of this.items) {
                total += item.totalCargos;
            }
            return total;
        },
        abonoTotal() {
            let total = 0;
            for (let item of this.items) {
                total += item.totalAbonos;
            }
            return total;
        }
    }
}

</script>

<style scope>
.cargo {
    color: red;
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