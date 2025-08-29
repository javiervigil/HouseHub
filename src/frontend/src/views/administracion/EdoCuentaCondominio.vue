<template>
    <Toast />
    <ConfirmPopup></ConfirmPopup>

    <br /><b style="color: var(--p-tabs-tab-active-color);">Estado de Cuenta </b>

    <div style="width: 70%;">
        <TableBar :showIcons="[0, 0, 0, 1, 1]" @refreshTable="refreshTable" @exportExcel="exportExcel" />

        <DataTable :value="items" sortField="loteId" stripedRows size="small" :sortOrder="1">
            <template #empty> No existen datos para esta tabla. </template>
            <template #loading> Cargando informacion.... </template>
            <Column field="name" header="Lote" style="width: 25%;"></Column>
            <Column field="totalCargos" header="Cargos" style="width: 25%;">
                <template #body="slotProps">
                    <div>
                        {{ formatCurrency(slotProps.data.totalCargos) }}
                    </div>
                </template>
            </Column>
            <Column field="totalAbonos" header="Abonos" style="width: 25%;">
                <template #body="slotProps">
                    <div>
                        {{ formatCurrency(slotProps.data.totalAbonos) }}
                    </div>
                </template>
            </Column>
            <Column header="Diferencia" style="width: 25%;">
                <template #body="slotProps">
                    <div :class="(slotProps.data.totalAbonos - slotProps.data.totalCargos) <= 0 ? 'cargo' : ''">
                        {{ formatCurrency(slotProps.data.totalAbonos - slotProps.data.totalCargos) }}
                    </div>
                </template>
            </Column>
            <ColumnGroup type="footer">
                <Row>
                    <Column footer="Totales:" footerStyle="text-align:right" />
                    <Column>
                        <template #footer="slotProps">
                            <div><b>{{ formatCurrency(cargoTotal) }}</b></div>
                        </template>
                    </Column>
                    <Column>
                        <template #footer="slotProps">
                            <div><b>{{ formatCurrency(abonoTotal) }}</b></div>
                        </template>
                    </Column>
                    <Column>
                        <template #footer="slotProps">
                            <div><b :class="(abonoTotal - cargoTotal) <= 0 ? 'cargo' : ''">
                                    {{ formatCurrency(abonoTotal - cargoTotal) }}</b></div>
                        </template>
                    </Column>
                </Row>
            </ColumnGroup>
        </DataTable>

    </div>
    <!-- <div class="chart-right" style="width: 30%;">
        <Chart type="bar" :data="chartData" />
    </div>  -->



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
        DataTable, Column, ColumnGroup, Row, TableBar, Dialog, InputText, Button, Form, Select, DatePicker, Message, ConfirmPopup, Toast, Chart
    },
    data() {
        return {
            plugins: [ChartDataLabels],


            titulo: ref(''),
            confirm: useConfirm(),
            toast: useToast(),
            selectedItems: ref([]),

            metaKey: ref(true),
            items: ref([]),




            chartData: null,
            chartOptions: null
        }
    },
    methods: {
        // llamadas al api
        async refreshTable() {
            try {
                if (this.selectedCondominio) {
                    this.items = (await apiService.getTransaccionesLoteTotalesByCondominio(this.selectedCondominio.id)).data;
                    this.chartData = this.setChartData();
                    this.chartOptions = this.setChartOptions();
                }
            } catch (error) {
                console.error("Failed to fetch TransaccionesLote:", error);
                this.toast.add({ severity: 'error', summary: 'Error de conexion con el servidor.', life: 3000 });
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
        exportExcel() {
            if (this.items.length === 0) {
                this.toast.add({ severity: 'warn', summary: 'No hay datos para exportar.', life: 3000 });
            } else {

                const itemsExport = this.items.map(item => {
                    return {
                        Lote: item.name,
                        Cargo: this.formatCurrency(item.totalCargos),
                        Abono: this.formatCurrency(item.totalAbonos),
                        Diferencia: this.formatCurrency(item.totalAbonos - item.totalCargos),
                    };
                });
                const worksheet = XLSX.utils.json_to_sheet(itemsExport);
                const workbook = XLSX.utils.book_new();
                XLSX.utils.book_append_sheet(workbook, worksheet, 'Estado de Cuenta');

                // Genera el archivo binario
                const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
                const data = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8' });

                // Descarga el archivo
                saveAs(data, 'Estado de Cuenta Condominio.xlsx');
            }
        }

    }, watch: {
        selectedCondominio(newValue, oldValue) {
            this.refreshTable();
        }
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