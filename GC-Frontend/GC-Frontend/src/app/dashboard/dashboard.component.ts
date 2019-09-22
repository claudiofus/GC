import {Component, OnInit} from '@angular/core';
import {formatCurrency} from '@angular/common';
import * as pluginDataLabels from 'chartjs-plugin-datalabels';
import {faPlay, faStop, faUsers, faWallet} from '@fortawesome/free-solid-svg-icons';
import {DashboardService} from './dashboard.service';
import {Utils} from '../../classes/utils';

@Component({
    selector   : 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls  : ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
    openBuildings: number;
    closedBuildings: number;
    workers: any;
    budget: number;
    backgroundColors = ['#ffcfdf', '#fefdca', '#e0f9b5', '#a5dee5', '#61c0bf', '#fae3d9', '#ffb6b9', '#aa96da',
        '#dbe2ef', '#8d6262', '#c1c0b9', '#ebcbae', '#95a792', '#45eba5', '#f2e9d0'];
    waitDiv: boolean;
    faPlay = faPlay;
    faStop = faStop;
    faUsers = faUsers;
    faWallet = faWallet;

    // TODO these 2 must be defined in the component
    public tooltips = {
        callbacks: {
            label: function (tooltipItem, data) {
                let dataLabel = data.labels[tooltipItem.index];
                const value = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                const value2 = formatCurrency(value, 'IT', '€', 'EUR');
                dataLabel += ' ' + value2;

                return dataLabel;
            }
        }
    };

    public plugins = {
        datalabels: {
            color    : 'grey',
            formatter: (value, ctx) => {
                let sum = 0;
                const dataArr = ctx.chart.data.datasets[0].data;
                dataArr.map(data => {
                    sum += data;
                });

                const percentage = (value * 100 / sum).toFixed(2) + '%';
                return percentage;
            }
        }
    };

    public hideGridLines = {
        xAxes: [{
            gridLines: {
                display: false
            },
            ticks: {
                beginAtZero: true
            }
        }],
        yAxes: [{
            gridLines: {
                display: false
            }
        }]
    };

    public chart1 = {
        labels  : [],
        datasets: [{
            data           : [],
            backgroundColor: this.backgroundColors
        }],
        options : {
            responsive         : true,
            maintainAspectRatio: false,
            layout             : {
                padding: 0
            },
            legend             : {
                display : true,
                position: 'bottom',
                labels  : {
                    fontColor: 'black'
                }
            },
            tooltips           : this.tooltips,
            plugins            : this.plugins
        },
        plugins : [pluginDataLabels]
    };

    public chart2 = {
        labels  : [],
        datasets: [{
            label: ['Ore di lavoro'],
            data : []
        }],
        options : {
            responsive         : true,
            maintainAspectRatio: false,
            scales             : this.hideGridLines,
            layout             : {
                padding: 0
            },
            legend             : {
                display : true,
                position: 'bottom',
                onClick : undefined,
                labels  : {
                    fontColor: 'black'
                }
            }
        }
    };

    public chart3 = {
        data   : [
            {data: [], label: 'Materiali'},
            {data: [], label: 'Operai'}
        ],
        labels : [],
        options: {
            scaleShowVerticalLines: false,
            responsive            : true,
            scales                : this.hideGridLines,
            tooltips              : {
                callbacks: {
                    label: function (tooltipItem, data) {
                        let dataLabel = data.datasets[tooltipItem.datasetIndex].label;
                        const value = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                        const value2 = formatCurrency(value, 'IT', '€', 'EUR');
                        dataLabel += ' ' + value2;

                        return dataLabel;
                    }
                }
            }
        }
    };

    constructor(private dashboardService: DashboardService) {
    }

    ngOnInit() {
        this.waitDiv = true;
        this.openBuildings = 0;
        this.closedBuildings = 0;
        this.workers = [];
        this.budget = 0;
        this.dashboardService.getAll().subscribe(
            buildings => {
                buildings.forEach(b => {
                    if (b.building.open) {
                        this.openBuildings++;
                    } else {
                        this.closedBuildings++;
                    }

                    const orderTot = Utils.round10(b.ordersTotal, -2);
                    const workerTot = Utils.round10(b.workersTotal, -2);
                    const budgetBuilding = b.building.reqAmount - orderTot - workerTot;
                    this.budget += budgetBuilding;
                    this.chart1.labels.push(b.building.name);
                    this.chart1.datasets[0].data.push(budgetBuilding);

                    this.chart3.labels.push(b.building.name);
                    this.chart3.data[0].data.push(orderTot);
                    this.chart3.data[1].data.push(workerTot);
                });
                this.waitDiv = false;
            }
        );

        this.dashboardService.getWorkload().subscribe(
            workers => {
                this.workers = workers;
                workers.forEach(w => {
                    this.chart2.labels.push(w.worker.surname + ' ' + w.worker.name);
                    this.chart2.datasets[0].data.push(w.workingHours);
                });
            }
        );
    }
}
