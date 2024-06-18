import moment from 'moment';
import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import Swal from 'sweetalert2';
import DataTable from 'react-data-table-component';
import axiosV1 from '../../utils/axiosV1';

function TransactionHistory() {
    const { register, handleSubmit, formState: { errors } } = useForm({
        mode: 'all',
        defaultValues: {
            accountNumber: '',
        },
    });
    const [isTableLoading, setIsTableLoading] = useState(false);
    const [transactionData, setTransactionData] = useState([]);
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

    const submitHandler = (data) => {
        setIsTableLoading(true);
        data.accountNumber = parseInt(data?.accountNumber, 10);
        axiosV1.post('/reports/transactions/all', null, {
            params: {
                accountNumber: data?.accountNumber,
            },
        }).then((response) => {
            setTransactionData(response?.data);
        }).catch((error) => {
            Swal.fire({
                icon: 'error',
                title: error?.response?.data?.responseMessage ?? 'Failed to fetch transaction history.',
                timer: 3000,
                showConfirmButton: false,
            });
        }).finally(() => {
            setIsTableLoading(false);
        });
    };

    return (
        <div className="card">
            <div className="card-header bg-transparent">
                <div className="row align-items-center">
                    <div className="col-xl-9 col-lg-8 col-md-7">
                        <h6 className="mb-0">List of Transactions</h6>
                    </div>
                    <div className="col-xl-3 col-lg-4 col-md-5 col-12 text-end">
                        <form onSubmit={handleSubmit(submitHandler)}>
                            <div className="input-group input-group-sm justify-content-start justify-content-md-end">
                                <input type="search" inputMode="numeric" className="form-control form-control-sm" id="accountNumber" {...register('accountNumber', { required: 'This field is required.', pattern: { value: /^\d+$/, message: 'Please enter valid account number.' } })} placeholder="Account Number" />
                                <button type="submit" className="btn btn-sm btn-primary">Submit</button>
                            </div>
                            {errors?.accountNumber && <small className="text-danger">{errors?.accountNumber?.message}</small>}
                        </form>
                    </div>
                </div>
            </div>
            <div className="card-body p-1">
                <DataTable
                    dense
                    striped
                    responsive
                    data={transactionData}
                    columns={columns}
                    pagination
                    progressPending={isTableLoading}
                />
            </div>
        </div>
    );
}

export default TransactionHistory;
