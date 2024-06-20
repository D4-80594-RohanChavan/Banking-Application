import { Modal } from 'react-bootstrap';
import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import Swal from 'sweetalert2';
import axiosV1 from '../../utils/axiosV1';
import BtnLoader from '../helpers/BtnLoader';

function BalanceEnquiry() {
    const [user] = useState(JSON.parse(localStorage.getItem('user')));
    const [isBtnLoading, setIsBtnLoading] = useState(false);
    const { register, handleSubmit, formState: { errors }, clearErrors, reset } = useForm({
        mode: 'all',
        defaultValues: {
            accountNumber: user?.accountNumber,
            password: '',
        },
    });
    const [show, setShow] = useState(false);
    const handleClose = () => {
        clearErrors();
        reset();
        setShow(false);
    };
    const handleShow = () => setShow(true);

    const submitHandler = (data) => {
        setIsBtnLoading(true);
        data.accountNumber = parseInt(data?.accountNumber, 10);
        axiosV1.post('/user/balanceEnquiry', data)
            .then((response) => {
                handleClose();
                Swal.fire({
                    icon: 'success',
                    title: `Your account balance is Rs. ${response?.data?.accountInfo?.accountBalance}`,
                    timer: 3000,
                    showConfirmButton: false,
                });
            })
            .catch((error) => {
                Swal.fire({
                    icon: 'error',
                    title: error?.response?.data?.responseMessage ?? 'Failed to fetch account balance.',
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
                    <Modal.Title>Balance Enquiry</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form onSubmit={handleSubmit(submitHandler)}>
                        <div className="form-group mb-3">
                            <label htmlFor="accountNumber" className="fw-semibold">Account Number <span className="text-danger">*</span></label>
                            <input type="text" inputMode="numeric" className="form-control form-control-sm" disabled id="accountNumber" {...register('accountNumber', { required: 'This field is required.', pattern: { value: /^\d+$/, message: 'Please enter valid account number.' } })} />
                            {errors?.accountNumber && <small className="text-danger">{errors?.accountNumber?.message}</small>}
                        </div>
                        <div className="form-group mb-3">
                            <label htmlFor="password" className="fw-semibold">Password <span className="text-danger">*</span></label>
                            <input type="password" className="form-control form-control-sm" id="password" {...register('password', { required: 'This field is required.', pattern: { value: /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/, message: 'Please enter valid password.' } })} />
                            {errors?.password && <small className="text-danger">{errors?.password?.message}</small>}
                        </div>
                        <div className="text-end">
                            {isBtnLoading ? <BtnLoader /> : <button type="submit" className="btn btn-sm btn-primary">Submit</button>}
                        </div>
                    </form>
                </Modal.Body>
            </Modal>
            <button type="button" className="btn btn-sm btn-outline-primary me-1" onClick={handleShow}>Balance Enquiry</button>
        </>
    );
}

export default BalanceEnquiry;
