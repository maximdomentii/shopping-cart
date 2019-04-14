import React, { Component } from "react";
import { Link, withRouter } from "react-router-dom";
import { Nav, Navbar, NavItem } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";
import axios from 'axios'
import "./App.css";
import Routes from "./Routes";

class App extends Component {
   constructor(props) {
        super(props);

        this.state = {
            isAuthenticated: localStorage.getItem("token") && localStorage.getItem("token").startsWith("Bearer") ?
                true
                : false,
            token: localStorage.getItem("token")
        };

        var self = this;
        axios.defaults.baseURL = 'http://localhost:8762/';
        axios.defaults.headers.common = {'Content-Type': 'application/json'};
        axios.interceptors.request.use(function (config) {
            if (self.state.token){
                config.headers.Authorization = self.state.token;
            }
            return config;
        }, function (error) {
            return Promise.reject(error);
        });
        axios.interceptors.response.use(function (response) {
            return response;
        }, function (error) {
            if (!error.config.url.endsWith("/auth") && error.response.status === 401){
                console.error("Session expired!")
                alert("Your session expired. Please login again!")
                self.setUserAuthentication(false, "");
                self.props.history.push("/login");
            } else {
                return Promise.reject(error);
            }
        });
    }

    setUserAuthentication = (authenticated, token) => {
        localStorage.setItem("token", token);
        this.setState({
            isAuthenticated: authenticated,
            token: token
        });
    }

    render() {
        const childProps = {
            isAuthenticated: this.state.isAuthenticated,
            token: this.state.token,
            setUserAuthentication: this.setUserAuthentication
        };

        return (
            <div className="App container">
                <Navbar fluid collapseOnSelect>
                    <Navbar.Header>
                        <Navbar.Brand>
                            <Link to="/">Home</Link>
                        </Navbar.Brand>
                        <Navbar.Toggle />
                    </Navbar.Header>
                    <Navbar.Collapse>
                        <Nav pullRight>
                            {this.state.isAuthenticated
                                ? <LinkContainer to="/cart">
                                    <NavItem>Start shopping</NavItem>
                                  </LinkContainer>
                                : <LinkContainer to="/login">
                                    <NavItem>Login</NavItem>
                                  </LinkContainer>
                            }
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
                <Routes childProps={childProps} />
            </div>
        );
    }
}

export default withRouter(App);