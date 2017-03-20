<!DOCTYPE html>

<html ng-app="app">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Transaction linking</title>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" rel="stylesheet">

    <script type="text/javascript" src="//netdna.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/resources/css/transactionLinking.css"/>

</head>

<body>

    <h1> 1. Implementation <a href="transactionLinking"> Transaction linking </a> </h1>

    <br><h1>2. Test task "Transaction Linking" </h1> <br>
    <b> Overview </b> <br>
        You are building an online shop where you accept payments via bank transfers. The customer places an order on your web site and then pays by transferring money to your bank account.
    The purpose of the task is to build a service that links the bank transactions to the customer orders. <br>

    When the customer places an order, you get at least following information <br>
    <ul>
        <li>Customer id, first name, last name</li>
        <li>How much he must pay</li>
        <li>When the order was placed</li>
    </ul>
    The online shop instructs the customer to put a certain reference (in fact the Customer ID) in the reference field of its online bank.
    For bank transactions, every bank uses different format, but for the sake of the exercise, we will assume you get:
    <ul>
        <li>Date of the transaction</li>
        <li>Amount transferred</li>
        <li>Description: The format here varies greatly from bank to bank, but let’s assume it contains user name (fully or partially) and the reference number.</li>
    </ul>
        You are asked to build a system that will import these bank transactions and link them to the matching orders.
    Remember that once a customer has sent you money, it has to be dealt with, even if your system fails to automatically link the transaction (meaning someone will have to manually link it, a potentially time consuming operation).
    Output of the task
    An application that takes a list of orders and transactions in a specified format (see below). The application outputs:
    <ul>
        <li>The list of order – transactions that could be linked.</li>
        <li>What could not be linked</li>
        <li>Please also provide the input data you created (orders + transactions) in order to test your application.</li>
    </ul>

    Input data<br>
    <b>Orders:</b><br>
    In JSON, format:
    <pre>
    [
        {
            "date": "2014-02-23T18: 25: 43.511Z",
            "amount": 205,
            "customer": {
                "id": "P45875",
                "firstName": "John",
                "lastName": "Smith"
            }
        },
        {
            "date": "2014-02-21T18: 25: 43.511Z",
            "amount": 999.01,
            "customer": {
                "id": "P14987",
                "firstName": "Paul",
                "lastName": "Ham"
            }
        }
    ]
    </pre><br>

    <b>Transactions:</b><br>

    As a CSV file <br>
    <pre>
        Date;Amount;Description
        24/02/14,999,John Smith P45875
        24/02/14,49.20,Linda Ham P14
    </pre>

</body>
</html>