import { Modal } from 'react-bootstrap';
import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import Swal from 'sweetalert2';
import axiosV1 from '../../utils/axiosV1';
import BtnLoader from '../helpers/BtnLoader';

function DebitAmount() {
    const [user] = useState(JSON.parse(localStorage.getItem('user')));
    const [isBtnLoading, setIsBtnLoading] = useState(false);
    const { register, handleSubmit, formState: { errors }, clearErrors, reset } = useForm({
        mode: 'all',
        defaultValues: {
            accountNumber: user?.accountNumber,
            password: '',
            amount: '',
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
        data.amount = parseInt(data?.amount, 10);
        axiosV1.post('/user/debit', data)
            .then(() => {
                handleClose();
                Swal.fire({
                    icon: 'success',
                    title: 'Amount debited successfully.',
                    timer: 3000,
                    showConfirmButton: false,
                });
            })
            .catch((error) => {
                Swal.fire({
                    icon: 'error',
                    title: error?.response?.data?.responseMessage ?? 'Failed to debit amount.',
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
                    <Modal.Title>Debit Amount</Modal.Title>
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
                        <div className="form-group mb-3">
                            <label htmlFor="amount" className="fw-semibold">Amount <span className="text-danger">*</span></label>
                            <input type="text" className="form-control form-control-sm" id="amount" {...register('amount', { required: 'This field is required.', pattern: { value: /^(?:-(?:[1-9](?:\d{0,2}(?:,\d{3})+|\d*))|(?:0|(?:[1-9](?:\d{0,2}(?:,\d{3})+|\d*))))(?:.\d+|)$/, message: 'Please enter valid amount.' } })} />
                            {errors?.amount && <small className="text-danger">{errors?.amount?.message}</small>}
                        </div>
                        <div className="text-end">
                            {isBtnLoading ? <BtnLoader /> : <button type="submit" className="btn btn-sm btn-primary">Submit</button>}
                        </div>
                    </form>
                </Modal.Body>
            </Modal>
            <button type="button" className="btn btn-sm btn-outline-primary me-1" onClick={handleShow}>Debit Amount</button>
        </>
    );
}

export default DebitAmount;
