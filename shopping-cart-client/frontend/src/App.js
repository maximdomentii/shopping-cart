import React, { Component } from "react";
import { Link } from "react-router-dom";
import { Nav, Navbar, NavItem } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";
import "./App.css";
import Routes from "./Routes";
import logo from './img/home.png';

class App extends Component {
    constructor(props) {
        super(props);

        this.state = {
            isAuthenticated: false,
            token: ""
        };
    }

    userHasAuthenticated = (authenticated, token) => {
        this.setState({
            isAuthenticated: authenticated,
            token: token
        });
    }

    render() {
        const childProps = {
            isAuthenticated: this.state.isAuthenticated,
            token: this.state.token,
            userHasAuthenticated: this.userHasAuthenticated
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
                                ? ""
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

export default App;