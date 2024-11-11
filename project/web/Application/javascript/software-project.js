window.onload = function () {


    document.getElementById("loginInvestorBtn").addEventListener("click", investorLogin);
    document.getElementById("loginBrokerBtn").addEventListener("click", brokerLogin);

    document.getElementById("logoutInvestorBtn").addEventListener("click", investorLogout);
    document.getElementById("logoutBrokerBtn").addEventListener("click", brokerLogout);

    document.getElementById("registerInvestorBtn").addEventListener("click", createInvestor);
    document.getElementById("registerBrokerBtn").addEventListener("click", createBroker);

    document.getElementById("deleteInvestorBtn").addEventListener("click", deleteInvestor);
    document.getElementById("deleteBrokerBtn").addEventListener("click", deleteBroker);

    document.getElementById("updateInvestorBtn").addEventListener("click", updateInvestor);
    document.getElementById("updateBrokerBtn").addEventListener("click", updateBroker);

    document.getElementById("createPortfolioBtn").addEventListener("click", function() {
        const brokerId = document.getElementById("brokerId").value;
        createPortfolio(brokerId);});
    document.getElementById("allPortfoliosBtn").addEventListener("click", getPortfolios);
    document.getElementById("sellPortfolioBtn").addEventListener("click", sellPortfolio);

    document.getElementById("allBrokersBtn").addEventListener("click", getAllRegisteredBrokers);

    document.getElementById("createAssetBtn").addEventListener("click", createAsset);
    document.getElementById("allAssetsBtn").addEventListener("click", getAllAsset);

    document.getElementById("availableAssetsBtn").addEventListener("click", function() {
        const brokerId = document.getElementById("brokerId").value;
        getAvailableAssetsForBroker(globalBrokerId);
    });

}

globalInvestorId = null;
globalBrokerId = null;
globalAssetId= null;
globalInvestorUsername = null;
globalBrokerUsername = null;
globalBrokerCampanyName = null;
globalInvestorAccessToken = null;
globalBrokerAccessToken = null;


async function investorLogin() {
    let investorUsername = document.getElementById("loginUsername").value;
    let password = document.getElementById("loginPassword").value;

    let loginData = {
        "username": investorUsername, "password": password
    }
    try {
        const response = await fetch("http://localhost:8080/app/access/login/Investor", {
            method: 'post', headers: {
                'Accept': 'application/json', 'Content-Type': 'application/json'
            }, body: JSON.stringify(loginData),
        });

        const result = await response.json();
        if (result.error != null) {
            showStatusError(result);
            throw new Error("Error: log in " + result.status);
        }
        else {
            globalInvestorId = result.id;
            globalInvestorUsername = result.username;
            globalInvestorAccessToken = result.credential.accessToken;

            clearAll();
            showLoginInvestorData();
            console.log(result);
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

async function brokerLogin() {
    let brokerUsername = document.getElementById("loginBrokerUsername").value;
    let password = document.getElementById("loginBrokerPassword").value;

    let loginData = {
        "username": brokerUsername, "password": password
    }
    try {
        const response = await fetch("http://localhost:8080/app/access/login/Broker", {
            method: 'post', headers: {
                'Accept': 'application/json', 'Content-Type': 'application/json'
            }, body: JSON.stringify(loginData),
        });

        const result = await response.json();
        if (result.error != null) {
            showStatusErrorBroker(result);
            throw new Error("Error: log in " + result.status);
        }
        else{
            globalBrokerId = result.id;
            globalBrokerUsername = result.username;
            globalBrokerAccessToken = result.credential.accessToken;

            clearAll();
            showLoginBrokerData();
            console.log(result);
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

async function investorLogout() {
    try {
        const response = await fetch("http://localhost:8080/app/access/logout/Investor", {
            method: 'delete', headers: {
                'Accept': 'application/json', 'Content-Type': 'application/json',
                'accessToken': globalInvestorAccessToken.accessToken,
            },
        });

        const result = await response.json();

        globalInvestorId = null;
        globalInvestorUsername = null;
        globalInvestorAccessToken = null;
        clearAll();

        console.log(result);

    } catch (error) {
        console.error("Error:", error);
    }
}

async function brokerLogout() {
    try {
        const response = await fetch("http://localhost:8080/app/access/logout/Broker", {
            method: 'delete', headers: {
                'Accept': 'application/json', 'accessToken': globalBrokerAccessToken.accessToken,
                'Content-Type': 'application/json'
            },
        });

        const result = await response.json();

        globalBrokerId = null;
        globalBrokerUsername = null;
        globalBAccessToken = null;

        console.log(result);

    } catch (error) {
        console.error("Error:", error);
    }
}

async function createInvestor() {
    let investorFirstname = document.getElementById("regFirstname").value;
    let investorLastname = document.getElementById("regLastname").value;
    let investorUsername = document.getElementById("regUsername").value;
    let investorPassword = document.getElementById("regPassword").value;

    let investor = {
        "firstname": investorFirstname,
        "lastname": investorLastname,
        "username": investorUsername,
        "password": investorPassword
    }
    try {
        const response = await fetch("http://localhost:8080/app/investors/registration", {
            method: 'post', headers: {
                'Accept': 'application/json', 'Content-Type': 'application/json'
            }, body: JSON.stringify(investor),
        });

        const result = await response.json();
        if (result.error != null) {
            showStatusError(result);
            throw new Error("Error: create Investor " + result.status);
        }
        else{

            globalInvestorId = result.id;
            globalInvestorUsername = result.username;
            globalInvestorAccessToken = result.credential.accessToken;

            clearAll();
            showLoginInvestorData();
            console.log(result);
        }


    } catch (error) {
        console.error("Error:", error);
    }
}

async function createBroker() {
    let brokerCompanyName = document.getElementById("regBrokerCompanyName").value;
    let brokerUsername = document.getElementById("regBrokerUsername").value;
    let brokerPassword = document.getElementById("regBrokerPassword").value;

    let broker = {
        "company": brokerCompanyName,
        "username": brokerUsername,
        "password": brokerPassword
    }
    try {
        const response = await fetch("http://localhost:8080/app/brokers/registration", {
            method: 'post', headers: {
                'Accept': 'application/json', 'Content-Type': 'application/json'
            }, body: JSON.stringify(broker),
        });

        const result = await response.json();
        if (result.error != null) {
            showStatusErrorBroker(result);
            throw new Error("Error: create Broker " + result.status);
        }

        else{
            globalBrokerId = result.id;
            globalBrokerCampanyName = result.company;
            globalBrokerUsername = result.username;
            globalBrokerAccessToken = result.credential.accessToken;

            clearAll();
            showLoginBrokerData();
            console.log(result);}

    } catch (error) {
        console.error("Error:", error);
    }
}

async function updateInvestor() {
    let investorFirstname = document.getElementById("updateFirstname").value;
    let investorLastname = document.getElementById("updateLastname").value;
    let investorPassword = document.getElementById("updatePassword").value;

    let investor = {
        "firstname": investorFirstname,
        "lastname": investorLastname,
        "password": investorPassword
    }

    try {
        const response = await fetch("http://localhost:8080/app/investors/update/" + globalInvestorId,
            {
                method: 'put',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'accessToken': globalInvestorAccessToken,
                },
                body: JSON.stringify(investor),
            });
        console.log(globalInvestorAccessToken);

        const result = await response.json();
        if (result.error != null) {
            showStatusError(result);
            throw new Error("Error: updateInvestor " + result.status);
        }
        else{
            clearAll();
            console.log(result);
        }

    } catch (error) {
        console.error("Error:", error);
    }
}

async function updateBroker() {
    let brokerCompanyName = document.getElementById("updateBrokerCompanyName").value;
    let brokerUsername = document.getElementById("updateBrokerUsername").value;
    let brokerPassword = document.getElementById("updateBrokerPassword").value;

    let broker = {
        "company": brokerCompanyName,
        "username": brokerUsername,
        "password": brokerPassword
    }

    try {
        const response = await fetch("http://localhost:8080/app/brokers/update/" + globalBrokerId,
            {
                method: 'put',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'accessToken': globalBrokerAccessToken,
                },
                body: JSON.stringify(broker),
            });

        const result = await response.json();
        if (result.error != null) {
            showStatusErrorBroker(result);
            throw new Error("Error: updateBroker " + result.status);
        }

        else {
            clearAll();
            console.log(result);
        }

    } catch (error) {
        console.error("Error:", error);
    }
}

async function deleteInvestor()
{
    try {
        const response = await fetch("http://localhost:8080/app/investors/delete/" + globalInvestorId,
            {
                method: 'delete',
                headers: {
                    'Content-Type': 'application/json',
                    'accessToken': globalInvestorAccessToken,

                },
            });


        globalInvestorId = null;
        globalInvestorUsername = null;
        globalInvestorAccessToken = null;



    } catch (error) {
        console.error("Error:", error);
        console.log(error);
    }
}
async function deleteBroker()
{
    try {
        const response = await fetch("http://localhost:8080/app/brokers/delete/" + globalBrokerId,
            {
                method: 'delete',
                headers: {
                    'Content-Type': 'application/json',
                    'accessToken': globalBrokerAccessToken,
                },
            });

        globalBrokerId = null;
        globalBrokerUsername = null;
        globalBrokerAccessToken = null;

        document.getElementById("successDeleteMessage").innerText = "Delete success!";

        setTimeout(() => {
            document.getElementById("successDeleteMessage").innerText = "";
        }, 10000);

    } catch (error) {
        console.error("Error:", error);
    }
}

function showLoginInvestorData() {
    if (globalInvestorId != null) {
        document.getElementById("outInvestorId").innerHTML = globalInvestorId;
    }
    else {
        document.getElementById("outInvestorId").innerHTML = "Not defined";
    }

    if (globalInvestorUsername != null) {
        document.getElementById("outInvestorUsername").innerHTML = globalInvestorUsername;
    }
    else {
        document.getElementById("outInvestorUsername").innerHTML = "Not defined";
    }

    if (globalInvestorAccessToken != null) {
        document.getElementById("outInvestorAccessToken").innerHTML = JSON.stringify(globalInvestorAccessToken);
    }
    else {
        document.getElementById("outInvestorAccessToken").innerHTML = "Not defined";
    }
}

function showLoginBrokerData() {
    if (globalBrokerId != null) {
        document.getElementById("outBrokerId").innerHTML = globalBrokerId;
    }
    else {
        document.getElementById("outBrokerId").innerHTML = "Not defined";
    }

    if (globalBrokerUsername != null) {
        document.getElementById("outBrokerUsername").innerHTML = globalBrokerUsername;
    }
    else {
        document.getElementById("outBrokerUsername").innerHTML = "Not defined";
    }

    if (globalBrokerAccessToken != null) {
        document.getElementById("outBrokerAccessToken").innerHTML = JSON.stringify(globalBrokerAccessToken);
    }
    else {
        document.getElementById("outBrokerAccessToken").innerHTML = "Not defined";
    }
}
function clearAll()
{
    document.getElementById("error").innerHTML = "";

    document.getElementById("loginUsername").value = "";
    document.getElementById("loginPassword").value = "";
    document.getElementById("loginBrokerUsername").value = "";
    document.getElementById("loginBrokerPassword").value = "";

    document.getElementById("regFirstname").value = "";
    document.getElementById("regLastname").value = "";
    document.getElementById("regUsername").value = "";
    document.getElementById("regPassword").value = "";

    document.getElementById("regBrokerCompanyName").value = "";
    document.getElementById("regBrokerUsername").value = "";
    document.getElementById("regBrokerPassword").value = "";


    document.getElementById("updateFirstname").value = "";
    document.getElementById("updateLastname").value = "";
    document.getElementById("updatePassword").value = "";
    document.getElementById("brokerId").value = "";

    document.getElementById("updateBrokerCompanyName").value = "";
    document.getElementById("updateBrokerUsername").value = "";
    document.getElementById("updateBrokerPassword").value = "";


    document.getElementById("assetName").value = "";
    document.getElementById("assetKind").value = "";

}

function showStatusError(result) {
    const errorElement = document.getElementById("error");
    clearAll();

    if (result.message.includes("null")) {
        errorElement.innerHTML = "ERROR: The data provided are not correct, please try again!";
    } else if (result.message.includes("400")) {
        errorElement.innerHTML = "ERROR: The data provided are not correct, please try again!";
    } else if (result.message.includes("500")) {
        errorElement.innerHTML = "ERROR: Internal Server Error occurred.";
    } else if (result.message.includes("401")) {
        errorElement.innerHTML = "ERROR: Unauthorized access.";
    }
    else {
        errorElement.innerHTML = "An unexpected error occurred. Please verify your data entered again!";
    }

    setTimeout(() => {
        errorElement.innerHTML = "";
    }, 60000);
}
function showStatusErrorBroker(result) {
    const errorElement = document.getElementById("errorBroker");

    if (result.message.includes("null")) {
        clearAll();
        errorElement.innerHTML = "ERROR: The data provided for the registration or to login are not correct, please try again!";
        return;
    }
    errorElement.innerHTML = "ERROR: An unexpected error occurred.";
    clearAll();

    // Check for specific HTTP status codes
    if (result.message.includes("500")) {
        errorElement.innerHTML = "ERROR: Internal Server Error occurred.";
        clearAll();
        return;
    }
    errorElement.innerHTML = "ERROR: An unexpected error occurred.";
    clearAll();

    if (result.message.includes("401")) {
        errorElement.innerHTML = "ERROR: Unauthorized access.";
        clearAll();
        return;
    }
    errorElement.innerHTML = "ERROR: An unexpected error occurred.";
    clearAll();

    if(result.message){
        errorElement.innerHTML = "ERROR: An unexpected error occurred. please verify your data!"
        clearAll();
        return;
    }


    setTimeout(() => {
        errorElement.innerHTML = "";
    }, 60000);
}

async function getAllRegisteredBrokers() {
    try {
        const response = await fetch("http://localhost:8080/app/brokers/getBrokers", {
            method: 'get',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalInvestorAccessToken
            },
        });

        const result = await response.json();
        showAllRegisteredBrokers(result);
        console.log(result);

        if (result.error != null) {
            showStatusErrorBroker(result);
            throw new Error("Error: getting all brokers " + result.status);
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

let assetIds = []; // Globales Array für die ausgewählten Asset-IDs

function showAllRegisteredBrokers(brokerlist) {
    let str = "";
    brokerlist.forEach(broker => {
        str += `<div>Broker: ${broker.username}</div>`;
        if (broker.assets && broker.assets.length > 0) {
            str += "<ul>";
            broker.assets.forEach(asset => {
                str += `<li>${asset.name} - <button onclick="selectAsset(${asset.assetId})">Select</button></li>`;
            });
            str += "</ul>";
        }
        str += `<button onclick="selectBroker(${broker.id})">Select Broker</button><br/><br/>`;
    });

    document.getElementById("outAllRegisteredBrokers").innerHTML = str;
}

async function selectBroker(brokerId) {
    document.getElementById("brokerId").value = brokerId;

    assetIds = [];
    await getAvailableAssetsForBroker(brokerId);
}

function selectAsset(assetId) {
    if (!assetId) {
        console.error("Error: Asset ID is undefined or null.");
        return;
    }

    if (!assetIds.includes(assetId)) {
        assetIds.push(assetId);
    }

    let selectedAssetsStr = "Selected Assets: ";
    if (assetIds.length === 0) {
        selectedAssetsStr += "None";
    } else {
        selectedAssetsStr += assetIds.join(", ");
    }
    document.getElementById("selectedAssets").innerHTML = selectedAssetsStr;
}


async function createPortfolio() {
    let brokerId = document.getElementById("brokerId").value;

    let portfolio = {
        "investorId": globalInvestorId,
        "brokerId": brokerId,
        "assetIds": assetIds
    };

    if (assetIds.length === 0) {
        try {
            const response = await fetch(`http://localhost:8080/app/asset/broker/${brokerId}/available`, {
                method: 'get',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'accessToken': globalInvestorAccessToken
                },
            });

            const result = await response.json();
            assetIds = result.map(asset => asset.assetId);
            portfolio.assetIds = assetIds;

            if (result.error != null) {
                showStatusErrorBroker(result);
                throw new Error("Error: create portfolio" + result.status);
            }

            else{
                console.log(result);
            }
        } catch (error) {
            console.error("Error fetching all assets for the broker:", error);
            return;
        }
    }

    try {
        const response = await fetch(`http://localhost:8080/app/portfolio/${globalInvestorId}/new_portfolio/${brokerId}`, {
            method: 'post',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalInvestorAccessToken
            },
            body: JSON.stringify(portfolio),
        });

        const result = await response.json();
        if (result.error != null) {
            showStatusErrorBroker(result);
            throw new Error("Error: createPortfolio " + result.status);
        } else {
            console.log(result); // Handle successful response
        }

    } catch (error) {
        console.error("Error:", error); // Handle fetch errors
    }
}



async function getPortfolios() {
    try {
        const response = await fetch("http://localhost:8080/app/portfolio/" + globalInvestorId + "/All_portfolios", {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalInvestorAccessToken
            }
        });

        const result = await response.json();
        if (result.error != null) {
            showStatusError(result);
            throw new Error("Error: getMyPortfolios " + result.status);
        } else {
            const outputElement = document.getElementById("outPortfolios");
            if (result.length === 0) {
                outputElement.innerText = "No Portfolios found!";
                outputElement.style.color = "red";
                setTimeout(() => {
                    outputElement.innerText = "";
                }, 10000);
            } else {
                outputElement.innerHTML = JSON.stringify(result, null, 2);
                outputElement.style.color = "black";
            }
            console.log(result);
        }

    } catch (error) {
        console.error("Error:", error);
        const errorMessageElement = document.getElementById("errorMessage");
        errorMessageElement.innerText = "Error: " + error.message;
        errorMessageElement.style.color = "red";

        setTimeout(() => {
            errorMessageElement.innerText = "";
        }, 10000);
    }
}



async function sellPortfolio() {
    let portfolio = {
        "investorId": globalInvestorId
    };

    try {
        const response = await fetch("http://localhost:8080/app/portfolio/" + globalInvestorId + "/delete_Portfolio", {
            method: 'delete',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalInvestorAccessToken
            },
            body: JSON.stringify(portfolio),
        });
             document.getElementById("successMessage").innerText = "Delete success!";

        setTimeout(() => {
            document.getElementById("successMessage").innerText = "";
        }, 10000);

    } catch (error) {
        console.error("Error:", error);
    }

}

async function getAvailableAssetsForBroker(brokerId) {
    console.log("Fetching available assets for broker:", brokerId);
    try {
        const response = await fetch("http://localhost:8080/app/asset/broker/" + brokerId + "/available", {
            method: 'get',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalInvestorAccessToken
            },
        });

        const result = await response.json();
        console.log("API response:", result); // Logge die gesamte API-Antwort
        if (response.status === 200) {
            showAvailableAssets(result);
        } else {
            throw new Error(`Error: Failed to fetch assets. Status ${response.status}`);
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

function showAvailableAssets(assets) {
    let str = "<h3>Available Assets</h3>";
    if (assets.length > 0) {
        str += "<ul>";
        assets.forEach(asset => {
            if (asset.asset_id) {
                str += `<li>${asset.name} (${asset.kind}) - ID: ${asset.asset_id} <button onclick="selectAsset('${asset.asset_id}')">Select</button></li>`;
                console.log('Asset ID:', asset.asset_id);
            } else {
                console.warn('Asset without ID:', asset);
            }
        });
        str += "</ul>";
    } else {
        str += "<div>No assets available for this broker</div>";
    }

    document.getElementById("availableAssets").innerHTML = str;
}



async function createAsset() {
    let assetName = document.getElementById("assetName").value;
    let assetKind = document.getElementById("assetKind").value;

    let asset = {
        "brokerId": globalBrokerId,
        "name":assetName,
        "kind":assetKind,

    };

    try {
        const response = await fetch("http://localhost:8080/app/asset/" + globalBrokerId + "/new_Asset", {
            method: 'post',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalBrokerAccessToken,
            },
            body: JSON.stringify(asset),
        });
        const result = await response.json();
        if (result.error != null) {
            showStatusError(result);
            throw new Error("Error: createAsset " + result.status);
        }

        else{
            console.log(result);
        }

    } catch (error) {
        console.error("Error:", error);
    }
}

async function getAllAsset() {
    try {
        const response = await fetch(`http://localhost:8080/app/asset/${globalBrokerId}/All_assets`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalBrokerAccessToken,
            },
        });

        const result = await response.json();
        console.log(result); // Check the response in the console

        const assetTable = document.getElementById('assetTable');

        // Clear previous rows (optional, if you want to reset the table)
        assetTable.innerHTML = '';

        if (response.ok) {
            if (result.length === 0) {
                // Handle case where no assets are returned
                assetTable.innerHTML = '<tr><td colspan="3">No assets found</td></tr>';
            } else {
                // Populate the table with asset data
                result.forEach(asset => {
                    const row = assetTable.insertRow();
                    row.innerHTML = `
                        <td>${asset.name}</td>
                        <td>${asset.kind}</td>
                        <td><button onclick="deleteAsset(${asset.assetId})">Delete</button></td>
                    `;
                });
            }
        } else {
            throw new Error(`Failed to fetch assets: ${response.status}`);
        }
    } catch (error) {
        console.error("Error:", error);
        // Handle errors or display error messages as necessary
    }
}

async function deleteAsset(assetId) {
    let asset = {
        'brokerId': globalBrokerId,
        'assetId': assetId
    };

    try {
        const response = await fetch("http://localhost:8080/app/asset/" + globalBrokerId + "/delete_Asset/" + assetId, {
            method: 'delete',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'accessToken': globalBrokerAccessToken,
                'brokerId': globalBrokerId
            },
            body: JSON.stringify(asset),
        });

        const messageElement = document.getElementById("deleteAssetMessage");

        if (response.ok) {
            const row = document.querySelector(`button[onclick="deleteAsset(${assetId})"]`).parentNode.parentNode;
            row.parentNode.removeChild(row);

            messageElement.innerText = "Delete success!";
            messageElement.style.color = "green";

            setTimeout(() => {
                messageElement.innerText = "";
            }, 10000);
        } else if (response.status === 403) {
            messageElement.innerText = "Asset belongs to a broker and can't be deleted!";
            messageElement.style.color = "red";

            setTimeout(() => {
                messageElement.innerText = "";
            }, 10000);
        } else {
            throw new Error('Failed to delete asset');
        }
    } catch (error) {
        console.error("Error:", error);
        const messageElement = document.getElementById("deleteAssetMessage");
        messageElement.innerText = "An error occurred while deleting the asset.";
        messageElement.style.color = "red";

        setTimeout(() => {
            messageElement.innerText = "";
        }, 10000);
    }
}




