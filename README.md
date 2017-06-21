# Beyond sample app

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

Very basic sample app that can be used for manual integration testing of beyond [official apps](https://cdp.epages.works/job/publish-api-docs/Public_REST_API_Documentation/index.html#resources-official-apps).

It is useful for testing the oauth authorization code flow.

## Usage

The client id and secret of the app need to be configured via `ng.sample.clientId` and `ng.sample.clientSecret`.

The callback url that you need to configure on the official app reside under `/authorize-callback`.

The app is just able to retrieve products from the [product-management API](https://cdp.epages.works/job/publish-api-docs/Public_REST_API_Documentation/index.html#resources-products-listhttps://cdp.epages.works/job/publish-api-docs/Public_REST_API_Documentation/index.html#resources-products-list).
Thus it only requires the scope 'products.read'.

Sample configuration for an official app configuration for this app:
```json
{
	"name": "review app",
	"scopes": ["products.read"],
	"callbackUrl": "https://warm-mountain-31010.herokuapp.com/authorize-callback"
}
```

The app is just able to store a single token - so it cannot be used against multiple shops. 
Once a token is obtained the app provides the ability to retrieve products vis `/products` 

