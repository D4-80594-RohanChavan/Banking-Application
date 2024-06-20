import React, { useEffect, useState } from 'react';
import { FiLogOut } from 'react-icons/fi';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';
import DataTable from 'react-data-table-component';
import moment from 'moment';
import { useForm } from 'react-hook-form';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, ArcElement, Tooltip, Legend } from 'chart.js';
import { Bar, Doughnut } from 'react-chartjs-2';
import axiosV1 from '../../utils/axiosV1';
import DebitAmount from './DebitAmount';
import CreditAmount from './CreditAmount';
import FundTransfer from './FundTransfer';
import BalanceEnquiry from './BalanceEnquiry';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, ArcElement, Tooltip, Legend);

function Dashboard() {
    const navigate = useNavigate();
    const [user] = useState(JSON.parse(localStorage.getItem('user')));
    const [barChart, setBarChart] = useState({});
    const [doughnutChart, setDoughnutChart] = useState({});
    const [isTableLoading, setIsTableLoading] = useState(false);
    const { register, handleSubmit, formState: { errors } } = useForm({
        mode: 'all',
        defaultValues: {
            startDate: null,
            endDate: null,
        },
    });
    const [statementData, setStatementData] = useState([]);
    const columns = [
        {
            name: 'Transaction ID',
            selector: (row) => row?.transactionId,
            format: (row) => <span title={row?.transactionId ?? '-'}>{row?.transactionId ?? '-'}</span>,
            sortable: true,
            reorder: true,
        },
        {
            name: 'Type',
            selector: (row) => row?.transactionType,
            format: (row) => <span title={row?.transactionType ?? '-'} className={`badge fw-semibold text-bg-${row?.transactionType === 'CREDIT' ? 'success' : (row?.transactionType === 'DEBIT' ? 'danger' : 'secondary')}`}>{row?.transactionType ?? '-'}</span>,
            sortable: true,
            reorder: true,
        },
        {
            name: 'Amount',
            selector: (row) => row?.amount,
            format: (row) => <span title={row?.amount ?? '-'}>{row?.amount ?? '-'}</span>,
            sortable: true,
            reorder: true,
        },
        {
            name: 'Status',
            selector: (row) => row?.status,
            format: (row) => <span title={row?.status ?? '-'}>{row?.status === 'SUCCESS' ? <span className="badge fw-semibold rounded-pill text-bg-success">SUCCESS</span> : <span className="badge fw-semibold rounded-pill text-bg-danger">FAILED</span>}</span>,
            sortable: true,
            reorder: true,
        },
        {
            name: 'Date',
            selector: (row) => row?.createdAt,
            format: (row) => <span title={moment(row?.createdAt, 'YYYY-MM-DD').format('llll')}>{moment(row?.createdAt, 'YYYY-MM-DD').format('llll')}</span>,
            sortable: true,
            reorder: true,
        },
    ];

    const fetchBarChartData = () => {
        axiosV1.post('/reports/monthly-summary/user', null, {
            params: {
                accountNumber: parseInt(user?.accountNumber, 10),
            },
        }).then((response) => {
            setBarChart(response?.data);
        }).catch((error) => {
            Swal.fire({
                icon: 'error',
                title: error?.response?.data?.responseMessage ?? 'Failed to fetch yearly transaction stats.',
                timer: 3000,
                showConfirmButton: false,
            });
        });
    };

    const fetchDoughnutChartData = () => {
        axiosV1.post('/reports/monthly-summary/user/current', null, {
            params: {
                accountNumber: parseInt(user?.accountNumber, 10),
            },
        }).then((response) => {
            setDoughnutChart(response?.data);
        }).catch((error) => {
            Swal.fire({
                icon: 'error',
                title: error?.response?.data?.responseMessage ?? 'Failed to fetch monthly transaction stats.',
                timer: 3000,
                showConfirmButton: false,
            });
        });
    };

    useEffect(() => {
        document.title = 'Dashboard';
        fetchBarChartData();
        fetchDoughnutChartData();
    }, []);

    const signOutHandler = () => {
        axiosV1.post('/user/logout')
            .then(() => {
                delete axiosV1.defaults.headers.common.Authorization;
                localStorage.clear();
                navigate('/sign-in');
                Swal.fire({
                    icon: 'success',
                    title: 'Sign out successful.',
                    timer: 3000,
                    showConfirmButton: false,
                });
            })
            .catch((error) => {
                Swal.fire({
                    icon: 'error',
                    title: error?.response?.data?.responseMessage ?? 'Failed to sign out.',
                    timer: 3000,
                    showConfirmButton: false,
                });
            });
    };

    const submitHandler = (data) => {
        setIsTableLoading(true);
        axiosV1.get('/user/bankStatement', {
            params: {
                accountNumber: user?.accountNumber,
                startDate: data?.startDate,
                endDate: data?.endDate,
            },
        }).then((response) => {
            setStatementData(response?.data);
        }).catch((error) => {
            Swal.fire({
                icon: 'error',
                title: error?.response?.data?.responseMessage ?? 'Failed to fetch bank statement.',
                timer: 3000,
                showConfirmButton: false,
            });
        }).finally(() => {
            setIsTableLoading(false);
        });
    };

    const barChartOptions = {
        plugins: {
            title: {
                display: false,
            },
        },
        responsive: true,
        scales: {
            x: {
                stacked: true,
            },
            y: {
                stacked: true,
            },
        },
    };

    const barChartData = {
        labels: Object.values(barChart)?.map((val) => moment(val?.month, 'YYYY-MM').format('MMM-YY')),
        datasets: [
            {
                label: 'Debit',
                data: Object.values(barChart)?.map((val) => val?.totalDebits ?? 0),
                backgroundColor: '#0d6efd',
            },
            {
                label: 'Credit',
                data: Object.values(barChart)?.map((val) => val?.totalCredits ?? 0),
                backgroundColor: '#00b7ff',
            },
            {
                label: 'Transfer',
                data: Object.values(barChart)?.map((val) => val?.totalTransfers ?? 0),
                backgroundColor: '#5beeff',
            },
        ],
    };

    const doughnutChartData = {
        labels: ['Debit', 'Credit', 'Transfer'],
        datasets: [
            {
                data: [doughnutChart?.totalDebits ?? 0, doughnutChart?.totalCredits ?? 0, doughnutChart?.totalTransfers ?? 0],
                backgroundColor: ['#0d6efd', '#00b7ff', '#5beeff'],
            },
        ],
    };

    return (
        <div className="container-fluid">
            <div className="row mb-3 py-2 text-bg-primary sticky-top">
                <div className="col-6">
                <img src="/src/assets/ui.png" alt="Krasv" width="220px" />
                </div>
                <div className="col-6 text-end">
                    <button type="button" title="Sign Out" className="btn btn-sm btn-outline-light" onClick={signOutHandler}><FiLogOut /> {user?.name}</button>
                </div>
            </div>
            <div className="row">
                <div className="col-12 mb-3">
                    <div className="card">
                        <div className="card-header bg-transparent">
                            <div className="row align-items-center">
                                <div className="col-4">
                                    <h6 className="mb-0">Profile</h6>
                                </div>
                                <div className="col-md-8 col-12 text-end">
                                    <div className="d-flex justify-content-end">
                                        <div className="">
                                            <BalanceEnquiry />
                                        </div>
                                        <div className="">
                                            <DebitAmount />
                                        </div>
                                        <div className="">
                                            <CreditAmount />
                                        </div>
                                        <div className="">
                                            <FundTransfer />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="card-body">
                            <div className="row g-3">
                                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                                    <p className="mb-0"><span className="fw-semibold">Account Number:</span> {user?.accountNumber ?? ''}</p>
                                </div>
                                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                                    <p className="mb-0"><span className="fw-semibold">Name:</span> {user?.name ?? ''}</p>
                                </div>
                                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                                    <p className="mb-0"><span className="fw-semibold">Gender:</span> {user?.gender ?? ''}</p>
                                </div>
                                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                                    <p className="mb-0"><span className="fw-semibold">Email:</span> {user?.email ?? ''}</p>
                                </div>
                                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                                    <p className="mb-0"><span className="fw-semibold">Phone Number:</span> {user?.phoneNumber ?? ''}</p>
                                </div>
                                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                                    <p className="mb-0"><span className="fw-semibold">Alternate Phone Number:</span> {user?.alternativePhoneNumber ?? ''}</p>
                                </div>
                                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                                    <p className="mb-0"><span className="fw-semibold">State:</span> {user?.stateOfOrigin ?? ''}</p>
                                </div>
                                <div className="col-xl-8 col-lg-8 col-12">
                                    <p className="mb-0"><span className="fw-semibold">Address:</span> {user?.address ?? ''}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="col-md-8 col-12 mb-3">
                    <div className="card h-100">
                        <div className="card-header bg-transparent">
                            <h6 className="mb-0">Yearly Transaction Stats</h6>
                        </div>
                        <div className="card-body d-flex align-items-center">
                            <Bar options={barChartOptions} data={barChartData} height={125} />
                        </div>
                    </div>
                </div>
                <div className="col-md-4 col-12 mb-3">
                    <div className="card h-100">
                        <div className="card-header bg-transparent">
                            <h6 className="mb-0">Monthly Transaction Stats</h6>
                        </div>
                        <div className="card-body d-flex align-items-center">
                            <Doughnut data={doughnutChartData} height={125} />
                        </div>
                    </div>
                </div>
                <div className="col-12 mb-3">
                    <div className="card">
                        <div className="card-header bg-transparent">
                            <div className="row align-items-center">
                                <div className="col-6">
                                    <h6 className="mb-0">Bank Statement</h6>
                                </div>
                                <div className="col-md-6 col-12 text-end">
                                    <form onSubmit={handleSubmit(submitHandler)}>
                                        <div className="input-group input-group-sm justify-content-start justify-content-md-end">
                                            <input type="date" className="form-control form-control-sm" style={{ maxWidth: '150px' }} id="startDate" {...register('startDate', { required: 'This start date is required.' })} />
                                            <input type="date" className="form-control form-control-sm" style={{ maxWidth: '150px' }} id="endDate" {...register('endDate', { required: 'This end date is required.' })} />
                                            <button type="submit" className="btn btn-sm btn-primary">Submit</button>
                                        </div>
                                        {(errors?.startDate || errors?.endDate) && <small className="text-danger">{errors?.startDate?.message || errors?.endDate?.message}</small>}
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div className="card-body p-1">
                            <DataTable
                                dense
                                striped
                                responsive
                                data={statementData}
                                columns={columns}
                                pagination
                                progressPending={isTableLoading}
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Dashboard;
