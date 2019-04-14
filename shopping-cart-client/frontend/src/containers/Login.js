import React, { Component } from "react";
import { FormGroup, FormControl } from "react-bootstrap";
import axios from 'axios'
import LoaderButton from "../components/LoaderButton";
import "./Login.css";

export default class Login extends Component {
    constructor(props) {
        super(props);

        this.state = {
            isLoading: false,
            username: "",
            password: ""
        };
    }

    validateForm() {
        return this.state.username.length > 0 && this.state.password.length > 0;
    }

    handleChange = event => {
        this.setState({
            [event.target.id]: event.target.value
        });
    }

    handleSubmit = event => {
        var self = this;
        event.preventDefault();

        this.setState({ isLoading: true });

        axios.post('auth', {
            username: this.state.username,
            password: this.state.password
        }).then(response => {
            self.props.setUserAuthentication(true, response.headers.authorization);
            self.props.history.push("/");
        }, function (error) {
            self.setState({ isLoading: false });
            if (error.response.status === 401){
                alert("Invalid credentials!");
            } else {
                alert("Service temporally unavailable. Try again later!");
            }
        }).catch(function (error) {
            console.error(error);
            self.setState({ isLoading: false });
        });
    }

    render() {
        return (
            <div className="Login">
                <form onSubmit={this.handleSubmit}>
                    <FormGroup controlId="username" bsSize="large">
                        <FormControl
                            autoFocus
                            type="text"
                            placeholder="Username"
                            value={this.state.username}
                            onChange={this.handleChange}
                        />
                    </FormGroup>
                    <FormGroup controlId="password" bsSize="large">
                        <FormControl
                            value={this.state.password}
                            onChange={this.handleChange}
                            type="password"
                            placeholder="Password"
                        />
                    </FormGroup>
                    <LoaderButton
                        block
                        bsSize="large"
                        disabled={!this.validateForm()}
                        type="submit"
                        isLoading={this.state.isLoading}
                        text="Login"
                        loadingText="Logging in…"
                    />
                </form>
            </div>
        );
    }
}