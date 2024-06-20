import React, { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import Swal from 'sweetalert2';
import { Link, useNavigate } from 'react-router-dom';
import { FiEye, FiEyeOff } from 'react-icons/fi';
import axiosV1 from '../../utils/axiosV1';

function SignIn() {
    const navigate = useNavigate();
    const [isBtnLoading, setIsBtnLoading] = useState(false);
    const [showPass, setShowPass] = useState(false);
    const { register, handleSubmit, formState: { errors }, clearErrors, reset } = useForm({
        mode: 'all',
        defaultValues: {
            accountNumber: '',
            email: '',
            password: '',
        },
    });

    useEffect(() => {
        document.title = 'Sign In';
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
        data.accountNumber = parseInt(data?.accountNumber, 10);
        axiosV1.post('/user/signin', data)
            .then((response) => {
                clearErrors();
                reset();
                localStorage.setItem('user', JSON.stringify(response?.data));
                localStorage.setItem('token', `Bearer ${response?.data?.jwt}`);
                axiosV1.defaults.headers.common.Authorization = `Bearer ${response?.data?.jwt}`;
                navigate('/dashboard');
            })
            .catch((error) => {
                Swal.fire({
                    icon: 'error',
                    title: error?.response?.data?.responseMessage ?? 'Failed to sign in.',
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
            <div className="row justify-content-center align-items-center vh-100">
                <div className="col-xl-4 col-lg-5 col-md-6 col-12">
                    <div className="card">
                        <div className="card-body">
                            <h4 className="text-primary text-center mb-3">Sign In</h4>
                            <form onSubmit={handleSubmit(submitHandler)}>
                                <div className="form-group mb-3">
                                    <label htmlFor="accountNumber" className="fw-semibold">Account Number <span className="text-danger">*</span></label>
                                    <input type="text" inputMode="numeric" className="form-control form-control-sm" id="accountNumber" {...register('accountNumber', { required: 'This field is required.', pattern: { value: /^\d+$/, message: 'Please enter valid account number.' } })} />
                                    {errors?.accountNumber && <small className="text-danger">{errors?.accountNumber?.message}</small>}
                                </div>
                                <div className="form-group mb-3">
                                    <label htmlFor="email" className="fw-semibold">Email <span className="text-danger">*</span></label>
                                    <input type="email" className="form-control form-control-sm" id="email" {...register('email', { required: 'This field is required.', pattern: { value: /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/, message: 'Please enter valid email.' } })} />
                                    {errors?.email && <small className="text-danger">{errors?.email?.message}</small>}
                                </div>
                                <div className="form-group mb-3">
                                    <label htmlFor="password" className="fw-semibold">Password <span className="text-danger">*</span></label>
                                    <input type="password" className="form-control form-control-sm" id="password" {...register('password', { required: 'This field is required.', pattern: { value: /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/, message: 'Please enter valid password.' } })} />
                                    <span className="showPass" onClick={showPassHandler} role="button" tabIndex={0} aria-hidden="true">{showPass ? <FiEyeOff /> : <FiEye />}</span>
                                    {errors?.password && <small className="text-danger">{errors?.password?.message}</small>}
                                </div>
                                {
                                    isBtnLoading ? (
                                        <button className="btn btn-sm btn-secondary w-100" type="button" disabled>
                                            <span className="spinner-border spinner-border-sm me-1" aria-hidden="true" />
                                            <span role="status">Loading</span>
                                        </button>
                                    ) : <button type="submit" className="btn btn-sm btn-primary w-100">Submit</button>
                                }
                            </form>
                        </div>
                    </div>
                    <div className="text-center">
                        <small className="text-muted">Don't have an account? <Link to="/sign-up" className="text-dark text-decoration-none fw-semibold">Sign Up</Link></small>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default SignIn;