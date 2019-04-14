import React, { Component } from "react";
import { Button, FormGroup, FormControl } from "react-bootstrap";
import axios from 'axios'
import LoaderButton from "../components/LoaderButton";
import "./Cart.css";

export default class Cart extends Component {
    constructor(props) {
        super(props);

        this.state = {
            receiptIsLoading: false,
            isLoading: false,
            barcode: "",
            products: [],
            receipt: {}
        };
    }

    validateForm() {
        return this.state.barcode.length > 0;
    }

    handleChange = event => {
        this.setState({
            [event.target.id]: event.target.value
        });
    }

    handleSubmit = event => {
        event.preventDefault();

        var self = this;

        this.setState({ isLoading: true });

        axios.get('shopcart/checkProduct/' + this.state.barcode).then(response => {
            self.setState({
                products: self.state.products.concat(response.data),
                barcode: "",
                isLoading: false,
                receipt: {}
            })
        }, function (error) {
            console.error(error);
            self.setState({
                isLoading: false,
                barcode: ""
            });
            if (error.response.status === 404){
                alert("Product unavailable!");
            } else {
                alert("Internal server error. Contact the administrator!");
            }
        }).catch(function (error) {
            console.error(error);
            self.setState({ isLoading: false });
        });
    }

    resetCart = event => {
        event.preventDefault();

        this.setState({
            receiptIsLoading: false,
            isLoading: false,
            barcode: "",
            products: [],
            receipt: {}
        })
    }
    
    getReceipt = event => {
        event.preventDefault();

        var self = this;

        this.setState({ receiptIsLoading: true });

        var barcodes = this.state.products.map( product => {
            return product.barcode;
        })

        axios.get('shopcart/receipt?barcodes=' + barcodes.join(",")).then(response => {
            self.setState({
                receipt: response.data,
                receiptIsLoading: false
            })
        }, function (error) {
            console.error(error);
            self.setState({
                receiptIsLoading: false
            });
            alert("Internal server error. Contact the administrator!");
        }).catch(function (error) {
            console.error(error);
            self.setState({ isLoading: false });
        });
    }

    render() {
        return (
            <div className="Cart">
                <form onSubmit={this.handleSubmit}>
                    <FormGroup controlId="barcode" bsSize="large">
                        <FormControl
                            autoFocus
                            type="number"
                            placeholder="Barcode"
                            value={this.state.barcode}
                            onChange={this.handleChange}
                        />
                    </FormGroup>
                    <LoaderButton
                        block
                        bsSize="large"
                        disabled={!this.validateForm()}
                        type="submit"
                        isLoading={this.state.isLoading}
                        text="Add product"
                        loadingText="Checking barcode..."
                    />
                    <Button
                        block
                        bsSize="small"
                        disabled={this.state.products.length === 0}
                        type="button"
                        onClick={this.resetCart}
                    >Reset cart</Button>
                </form>
                <div className="product-list">
                    <ul>
                        {this.state.products.map((product, index) =>
                            <li key={index}>{product.name}</li>
                        )}
                    </ul>
                    <LoaderButton
                        block
                        bsSize="large"
                        disabled={this.state.products.length === 0}
                        type="button"
                        isLoading={this.state.receiptIsLoading}
                        text="Get receipt"
                        loadingText="Preparing receipt..."
                        onClick={this.getReceipt}
                    />
                </div>
                {this.state.receipt.total &&
                    <div className="receipt">
                        <div className="receipt-header">
                            <h1>R3PI</h1>
                            <h2>Shopping Cart</h2>
                            <p>{new Date().toUTCString()}</p>
                        </div>
                        <hr/>
                        {this.state.receipt.products.map( (product, index) => {
                            return (
                                <div key={index} className="receipt-product">
                                    <div className="row">
                                        <div className="col-md-8"><b>{product.name}</b></div>
                                        <div className="col-md-4">{product.priceForOneProduct} $</div>
                                    </div>
                                    <div className="row">
                                        <div className="col-md-8">{"x" + product.cty}</div>
                                        <div className="col-md-4">{product.priceForAllProducts} $</div>
                                    </div>
                                    {product.discounts.map( (discount, dIdx) => {
                                        var productDiscount = discount.discountedAmount > 0 ? (
                                            <div key={dIdx} className="row">
                                                <div className="col-md-8">{discount.discountRuleName}</div>
                                                <div className="col-md-4">-{discount.discountedAmount} $</div>
                                            </div>
                                        ) : "";
                                        return productDiscount
                                    })
                                    }
                                    <hr/>
                                </div>
                            )})
                        }
                        <div className="row">
                            <div className="col-md-8">Subtotal:</div>
                            <div className="col-md-4">{this.state.receipt.subTotal} $</div>
                        </div>
                        {this.state.receipt.totalDiscount > 0 &&
                            <div className="row">
                                <div className="col-md-8">Discount:</div>
                                <div className="col-md-4">-{this.state.receipt.totalDiscount} $</div>
                            </div>
                        }
                        <div className="row">
                            <div className="col-md-8"><b>Total:</b></div>
                            <div className="col-md-4"><b>{this.state.receipt.total} $</b></div>
                        </div>
                        <hr/>
                        <span>Thank you for shopping with us!</span>
                    </div>
                }
            </div>
        );
    }
}