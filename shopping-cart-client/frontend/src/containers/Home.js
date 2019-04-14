import React, { Component } from "react";
import "./Home.css";
import home_img from "../img/r3pi.png"

export default class Home extends Component {
    render() {
        return (
            <div className="Home">
                <div className="lander-img">
                    <img src={home_img} alt={"R3PI"}></img>
                </div>
                <div className="lander-text">
                    <h1>Whelcome to Shopping Cart!</h1>
                </div>
            </div>
        );
    }
}