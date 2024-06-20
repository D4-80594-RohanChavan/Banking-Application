import React, { useEffect, useState } from 'react';
import { FiEdit, FiLogOut } from 'react-icons/fi';
import Swal from 'sweetalert2';
import { useNavigate } from 'react-router-dom';
import DataTable from 'react-data-table-component';
import moment from 'moment';
import { useForm } from 'react-hook-form';
import { Modal, Tab, Tabs } from 'react-bootstrap';
import axiosV1 from '../../utils/axiosV1';
import BtnLoader from '../helpers/BtnLoader';
import TransactionHistory from './TransactionHistory';
import AdminStats from './AdminStats';
import searchData from '../../utils/searchData';

function AdminDashboard() {
    const navigate = useNavigate();
    const [user] = useState(JSON.parse(localStorage.getItem('user')));
    const [isTableLoading, setIsTableLoading] = useState(false);
    const [activeUsers, setActiveUsers] = useState([]);
    const [filteredActiveUsers, setFilteredActiveUsers] = useState([]);
    const [searchActiveUsers, setSearchActiveUsers] = useState(null);
    const [inactiveUsers, setInactiveUsers] = useState([]);
    const [filteredInactiveUsers, setFilteredInactiveUsers] = useState([]);
    const [searchInactiveUsers, setSearchInactiveUsers] = useState(null);
    const [isBtnLoading, setIsBtnLoading] = useState(false);
    const { register, handleSubmit, formState: { errors }, clearErrors, reset, setValue } = useForm({
        mode: 'all',
        defaultValues: {
            accountNumber: '',
            name: '',
            aadharCard: '',
            panCard: '',
            email: '',
            phoneNumber: '',
            alternativePhoneNumber: '',
            address: '',
            stateOfOrigin: '',
        },
    });
    const [show, setShow] = useState(false);
    const handleClose = () => {
        clearErrors();
        reset();
        setShow(false);
    };
    const handleShow = () => setShow(true);

    useEffect(() => {
        setFilteredActiveUsers(activeUsers);
    }, [activeUsers]);

    useEffect(() => {
        setFilteredInactiveUsers(inactiveUsers);
    }, [inactiveUsers]);

    useEffect(() => {
        setFilteredActiveUsers(searchData(searchActiveUsers, activeUsers));
    }, [searchActiveUsers]);

    useEffect(() => {
        setFilteredInactiveUsers(searchData(searchInactiveUsers, inactiveUsers));
    }, [searchInactiveUsers]);

    const fetchUsers = (status) => {
        setIsTableLoading(true);
        axiosV1.post('/admin/usersbystatus', { status })
            .then((response) => {
                if (status === 'ACTIVE') {
                    setActiveUsers(response?.data ?? []);
                } else {
                    setInactiveUsers(response?.data ?? []);
                }
            })
            .catch((error) => {
                Swal.fire({
                    icon: 'error',
                    title: error?.response?.data?.responseMessage ?? 'Failed to fetch users.',
                    timer: 3000,
                    showConfirmButton: false,
                });
            })
            .finally(() => {
                setIsTableLoading(false);
            });
    };

    useEffect(() => {
        document.title = 'Admin Dashboard';
        fetchUsers('ACTIVE');
        fetchUsers('INACTIVE');
    }, []);

    const editHandler = (accountNumber) => {
        axiosV1.post('/admin/userbyaccountnumber', { accountNumber })
            .then((response) => {
                setValue('accountNumber', response?.data?.accountNumber ?? '');
                setValue('name', response?.data?.name ?? '');
                setValue('aadharCard', response?.data?.aadharCard ?? '');
                setValue('panCard', response?.data?.panCard ?? '');
                setValue('email', response?.data?.email ?? '');
                setValue('phoneNumber', response?.data?.phoneNumber ?? '');
                setValue('alternativePhoneNumber', response?.data?.alternativePhoneNumber ?? '');
                setValue('stateOfOrigin', response?.data?.stateOfOrigin ?? '');
                setValue('address', response?.data?.address ?? '');
                handleShow();
            })
            .catch((error) => {
                Swal.fire({
                    icon: 'error',
                    title: error?.response?.data?.responseMessage ?? 'Failed to fetch user details.',
                    timer: 3000,
                    showConfirmButton: false,
                });
            });
    };

    const statusChangeHandler = (status, accountNumber) => {
        axiosV1.post(status === 'ACTIVE' ? '/admin/inactiveUser' : '/admin/activateuser', { accountNumber })
            .then(() => {
                fetchUsers('ACTIVE');
                fetchUsers('INACTIVE');
                Swal.fire({
                    icon: 'success',
                    title: 'User status updated successfully.',
                    timer: 3000,
                    showConfirmButton: false,
                });
            })
            .catch((error) => {
                Swal.fire({
                    icon: 'error',
                    title: error?.response?.data?.responseMessage ?? 'Failed to update user status.',
                    timer: 3000,
                    showConfirmButton: false,
                });
            });
    };

    const columns = [
        {
            name: 'Action',
            cell: (row) => <button type="button" className="btn btn-sm btn-outline-primary" onClick={() => { editHandler(row?.accountNumber); }}><FiEdit /> Edit User</button>,
        },
        {
            name: 'Account Number',
            selector: (row) => row?.accountNumber,
            format: (row) => <span title={row?.accountNumber ?? '-'}>{row?.accountNumber ?? '-'}</span>,
            sortable: true,
            reorder: true,
        },
        {
            name: 'Name',
            selector: (row) => row?.name,
            format: (row) => <span title={row?.name ?? '-'}>{row?.name ?? '-'}</span>,
            sortable: true,
            reorder: true,
        },
        {
            name: 'Email',
            selector: (row) => row?.email,
            format: (row) => <span title={row?.email ?? '-'}>{row?.email ?? '-'}</span>,
            sortable: true,
            reorder: true,
        },
        {
            name: 'Phone Number',
            selector: (row) => row?.phoneNumber,
            format: (row) => <span title={row?.phoneNumber ?? '-'}>{row?.phoneNumber ?? '-'}</span>,
            sortable: true,
            reorder: true,
        },
        {
            name: 'Status',
            cell: (row) => (
                <div className="form-check form-switch">
                    <input className="form-check-input" type="checkbox" role="switch" id={`status${row?.id}`} checked={row?.status === 'ACTIVE'} disabled={row?.status === 'ACTIVE'} onChange={() => { statusChangeHandler(row?.status, row?.accountNumber); }} />
                </div>
            ),
            sortable: true,
            reorder: true,
        },
    ];

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

    const expandableRowsComponent = ({ data }) => (
        <div className="container-fluid p-3">
            <div className="row g-3">
                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                    <p className="mb-0"><span className="fw-semibold">Account Number:</span> {data?.accountNumber ?? '-'}</p>
                </div>
                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                    <p className="mb-0"><span className="fw-semibold">Account Balance:</span> Rs. {data?.accountBalance ?? '-'}</p>
                </div>
                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                    <p className="mb-0"><span className="fw-semibold">Name:</span> {data?.name ?? '-'}</p>
                </div>
                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                    <p className="mb-0"><span className="fw-semibold">Aadhar Card Number:</span> {data?.aadharCard ?? '-'}</p>
                </div>
                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                    <p className="mb-0"><span className="fw-semibold">PAN Card Number:</span> {data?.panCard ?? '-'}</p>
                </div>
                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                    <p className="mb-0"><span className="fw-semibold">Gender:</span> {data?.gender ?? '-'}</p>
                </div>
                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                    <p className="mb-0"><span className="fw-semibold">Email:</span> {data?.email ?? '-'}</p>
                </div>
                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                    <p className="mb-0"><span className="fw-semibold">Phone Number:</span> {data?.phoneNumber ?? '-'}</p>
                </div>
                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                    <p className="mb-0"><span className="fw-semibold">Alternate Phone Number:</span> {data?.alternativePhoneNumber ?? '-'}</p>
                </div>
                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                    <p className="mb-0"><span className="fw-semibold">State:</span> {data?.stateOfOrigin ?? '-'}</p>
                </div>
                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                    <p className="mb-0"><span className="fw-semibold">Created At:</span> {moment(data?.createdAt).format('llll')}</p>
                </div>
                <div className="col-xl-4 col-lg-4 col-md-6 col-12">
                    <p className="mb-0"><span className="fw-semibold">Updated At:</span> {moment(data?.modifiedAt).format('llll')}</p>
                </div>
                <div className="col-12">
                    <p className="mb-0"><span className="fw-semibold">Address:</span> {data?.address ?? '-'}</p>
                </div>
            </div>
        </div>
    );

    const submitHandler = (data) => {
        setIsBtnLoading(true);
        data.id = parseInt(data?.id, 10);
        data.phoneNumber = parseInt(data?.phoneNumber, 10);
        data.alternativePhoneNumber = parseInt(data?.alternativePhoneNumber, 10);
        data.aadharCard = parseInt(data?.aadharCard, 10);
        axiosV1.patch('/admin/updateuser', data)
            .then(() => {
                handleClose();
                fetchUsers('ACTIVE');
                fetchUsers('INACTIVE');
                Swal.fire({
                    icon: 'success',
                    title: 'Account details updated successfully.',
                    timer: 3000,
                    showConfirmButton: false,
                });
            })
            .catch((error) => {
                Swal.fire({
                    icon: 'error',
                    title: error?.response?.data?.responseMessage ?? 'Failed to update account details.',
                    timer: 3000,
                    showConfirmButton: false,
                });
            })
            .finally(() => {
                setIsBtnLoading(false);
            });
    };

    return (
        <>
            <Modal
                show={show}
                onHide={handleClose}
                backdrop="static"
                keyboard={false}
                centered
                scrollable
            >
                <Modal.Header closeButton>
                    <Modal.Title>Edit User</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form onSubmit={handleSubmit(submitHandler)}>
                        <input type="hidden" value="" {...register('accountNumber')} />
                        <div className="row g-3">
                            <div className="col-12">
                                <label htmlFor="name" className="fw-semibold">Name <span className="text-danger">*</span></label>
                                <input type="name" className="form-control form-control-sm" id="name" {...register('name', { required: 'This field is required.' })} />
                                {errors?.name && <small className="text-danger">{errors?.name?.message}</small>}
                            </div>
                            <div className="col-xl-6 col-lg-6 col-md-6 col-12">
                                <label htmlFor="aadharCard" className="fw-semibold">Aadhar Card Number <span className="text-danger">*</span></label>
                                <input type="text" className="form-control form-control-sm" id="aadharCard" {...register('aadharCard', { required: 'This field is required.', pattern: { value: /^\d{12}$/, message: 'Please enter valid aadhar card number.' } })} />
                                {errors?.aadharCard && <small className="text-danger">{errors?.aadharCard?.message}</small>}
                            </div>
                            <div className="col-xl-6 col-lg-6 col-md-6 col-12">
                                <label htmlFor="panCard" className="fw-semibold">PAN Card Number  <span className="text-danger">*</span></label>
                                <input type="text" className="form-control form-control-sm" id="panCard" {...register('panCard', { required: 'This field is required.', pattern: { value: /^[A-Z]{5}[0-9]{4}[A-Z]{1}$/, message: 'Please enter valid PAN card number.' } })} />
                                {errors?.panCard && <small className="text-danger">{errors?.panCard?.message}</small>}
                            </div>
                            <div className="col-xl-6 col-lg-6 col-md-6 col-12">
                                <label htmlFor="email" className="fw-semibold">Email <span className="text-danger">*</span></label>
                                <input type="email" className="form-control form-control-sm" id="email" {...register('email', { required: 'This field is required.', pattern: { value: /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/, message: 'Please enter valid email.' } })} />
                                {errors?.email && <small className="text-danger">{errors?.email?.message}</small>}
                            </div>
                            <div className="col-xl-6 col-lg-6 col-md-6 col-12">
                                <label htmlFor="phoneNumber" className="fw-semibold">Phone Number <span className="text-danger">*</span></label>
                                <input type="text" inputMode="numeric" className="form-control form-control-sm" id="phoneNumber" {...register('phoneNumber', { required: 'This field is required.', pattern: { value: /^\d{10}$/, message: 'Please enter valid phone number.' } })} />
                                {errors?.phoneNumber && <small className="text-danger">{errors?.phoneNumber?.message}</small>}
                            </div>
                            <div className="col-xl-6 col-lg-6 col-md-6 col-12">
                                <label htmlFor="alternativePhoneNumber" className="fw-semibold">Alternate Phone Number <span className="text-danger">*</span></label>
                                <input type="text" inputMode="numeric" className="form-control form-control-sm" id="alternativePhoneNumber" {...register('alternativePhoneNumber', { required: 'This field is required.', pattern: { value: /^\d{10}$/, message: 'Please enter valid phone number.' } })} />
                                {errors?.alternativePhoneNumber && <small className="text-danger">{errors?.alternativePhoneNumber?.message}</small>}
                            </div>
                            <div className="col-xl-6 col-lg-6 col-md-6 col-12">
                                <label htmlFor="stateOfOrigin" className="fw-semibold">State <span className="text-danger">*</span></label>
                                <input type="text" className="form-control form-control-sm" id="stateOfOrigin" {...register('stateOfOrigin', { required: 'This field is required.' })} />
                                {errors?.stateOfOrigin && <small className="text-danger">{errors?.stateOfOrigin?.message}</small>}
                            </div>
                            <div className="col-12">
                                <label htmlFor="address" className="fw-semibold">Address <span className="text-danger">*</span></label>
                                <textarea className="form-control form-control-sm" id="address" {...register('address', { required: 'This field is required.' })} />
                                {errors?.address && <small className="text-danger">{errors?.address?.message}</small>}
                            </div>
                            <div className="col-12 text-end">
                                {isBtnLoading ? <BtnLoader /> : <button type="submit" className="btn btn-sm btn-primary">Submit</button>}
                            </div>
                        </div>
                    </form>
                </Modal.Body>
            </Modal>
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
                    <div className="col-12">
                        <Tabs
                            variant="pills"
                            defaultActiveKey="dashboard"
                            id="dashboardTabs"
                            className="mb-3"
                            fill
                            justify
                        >
                            <Tab eventKey="dashboard" title="Dashboard">
                                <AdminStats />
                            </Tab>
                            <Tab eventKey="activeUsers" title="Active Users">
                                <div className="card">
                                    <div className="card-header bg-transparent">
                                        <div className="row align-items-center">
                                            <div className="col-xl-9 col-lg-8 col-md-7 col-12">
                                                <h6 className="mb-0">List of Active Users</h6>
                                            </div>
                                            <div className="col-xl-3 col-lg-4 col-md-5 col-12">
                                                <input type="search" name="searchActiveUsers" id="searchActiveUsers" className="form-control form-control-sm" placeholder="Search" value={searchActiveUsers} onChange={(e) => setSearchActiveUsers(e.target.value)} />
                                            </div>
                                        </div>
                                    </div>
                                    <div className="card-body p-1">
                                        <DataTable
                                            striped
                                            responsive
                                            data={filteredActiveUsers}
                                            columns={columns}
                                            pagination
                                            progressPending={isTableLoading}
                                            expandableRows
                                            expandableRowsComponent={expandableRowsComponent}
                                        />
                                    </div>
                                </div>
                            </Tab>
                            <Tab eventKey="inactiveUsers" title="Inactive Users">
                                <div className="card">
                                    <div className="card-header bg-transparent">
                                        <div className="row align-items-center">
                                            <div className="col-xl-9 col-lg-8 col-md-7 col-12">
                                                <h6 className="mb-0">List of Inactive Users</h6>
                                            </div>
                                            <div className="col-xl-3 col-lg-4 col-md-5 col-12">
                                                <input type="search" name="searchInactiveUsers" id="searchInactiveUsers" className="form-control form-control-sm" placeholder="Search" value={searchInactiveUsers} onChange={(e) => setSearchInactiveUsers(e.target.value)} />
                                            </div>
                                        </div>
                                    </div>
                                    <div className="card-body p-1">
                                        <DataTable
                                            striped
                                            responsive
                                            data={filteredInactiveUsers}
                                            columns={columns}
                                            pagination
                                            progressPending={isTableLoading}
                                            expandableRows
                                            expandableRowsComponent={expandableRowsComponent}
                                        />
                                    </div>
                                </div>
                            </Tab>
                            <Tab eventKey="transactionHistory" title="Transaction History">
                                <TransactionHistory />
                            </Tab>
                        </Tabs>
                    </div>
                </div>
            </div>
        </>
    );
}

export default AdminDashboard;
