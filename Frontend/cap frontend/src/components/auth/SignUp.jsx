import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { FiEye, FiEyeOff } from 'react-icons/fi';
import { Link, useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
import axiosV1 from '../../utils/axiosV1';
import BtnLoader from '../helpers/BtnLoader';

function SignUp() {
    const { navigate } = useNavigate();
    const [showPass, setShowPass] = useState(false);
    const [isBtnLoading, setIsBtnLoading] = useState(false);
    const { register, handleSubmit, formState: { errors }, clearErrors, reset } = useForm({
        mode: 'all',
        defaultValues: {
            name: '',
            gender: 'Male',
            email: '',
            password: '',
            phoneNumber: '',
            alternativePhoneNumber: '',
            aadharCard: '',
            panCard: '',
            address: '',
            stateOfOrigin: '',
            role: 'ROLE_USER',
        },
    });

    useEffect(() => {
        document.title = 'Sign Up';
        const token = localStorage.getItem('token');
        const user = JSON.parse(localStorage.getItem('user'));

        if (token && user && token === axiosV1.defaults.headers.common.Authorization) {
            navigate('/dashboard');
        } else {
            delete axiosV1.defaults.headers.common.Authorization;
        }
    }, []);

    const submitHandler = (data) => {
        setIsBtnLoading(true);
        data.phoneNumber = parseInt(data?.phoneNumber, 10);
        data.alternativePhoneNumber = parseInt(data?.alternativePhoneNumber, 10);
        data.aadharCard = parseInt(data?.aadharCard, 10);
        axiosV1.post('/user/createAccount', data)
            .then(() => {
                clearErrors();
                reset();
                Swal.fire({
                    icon: 'success',
                    title: 'Sign up successful.',
                    text: 'Account details will be sent to your email after admin approval.',
                    timer: 3000,
                    showConfirmButton: false,
                });
            })
            .catch((error) => {
                Swal.fire({
                    icon: 'error',
                    title: error?.response?.data?.responseMessage ?? 'Failed to sign up.',
                    timer: 3000,
                    showConfirmButton: false,
                });
            })
            .finally(() => {
                setIsBtnLoading(false);
            });
    };

    const showPassHandler = () => {
        setShowPass(!showPass);
        const passInp = document.getElementById('password');
        passInp.type = passInp.type === 'password' ? 'text' : 'password';
    };

    return (
        <div className="container-fluid bg-telegram">
            <div className="row justify-content-center align-items-center min-vh-100">
                <div className="col-xl-6 col-lg-7 col-md-8 col-12">
                    <div className="card">
                        <div className="card-body">
                            <h4 className="text-primary text-center mb-3">Sign Up</h4>
                            <form onSubmit={handleSubmit(submitHandler)}>
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
                                        <label htmlFor="password" className="fw-semibold">Password <span className="text-danger">*</span></label>
                                        <input type="password" className="form-control form-control-sm" id="password" {...register('password', { required: 'This field is required.', pattern: { value: /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/, message: 'Please enter valid password.' } })} />
                                        <span className="showPass" onClick={showPassHandler} role="button" tabIndex={0} aria-hidden="true">{showPass ? <FiEyeOff /> : <FiEye />}</span>
                                        {errors?.password && <small className="text-danger">{errors?.password?.message}</small>}
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
                                        <label htmlFor="gender" className="fw-semibold">Gender <span className="text-danger">*</span></label>
                                        <div id="gender">
                                            <div className="form-check form-check-inline">
                                                <input className="form-check-input" type="radio" id="male" value="Male" {...register('gender', { required: 'This field is required.' })} />
                                                <label className="form-check-label" htmlFor="male">Male</label>
                                            </div>
                                            <div className="form-check form-check-inline">
                                                <input className="form-check-input" type="radio" id="female" value="Female" {...register('gender', { required: 'This field is required.' })} />
                                                <label className="form-check-label" htmlFor="female">Female</label>
                                            </div>
                                            <div className="form-check form-check-inline">
                                                <input className="form-check-input" type="radio" id="transgender" value="Transgender" {...register('gender', { required: 'This field is required.' })} />
                                                <label className="form-check-label" htmlFor="transgender">Transgender</label>
                                            </div>
                                        </div>
                                        {errors?.gender && <small className="text-danger">{errors?.gender?.message}</small>}
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
                        </div>
                    </div>
                    <div className="text-center">
                        <small className="text-muted">Already have an account? <Link to="/sign-in" className="text-dark text-decoration-none fw-semibold">Sign In</Link></small>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default SignUp;
