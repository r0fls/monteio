# monteio

> **Archived.** This is an unmaintained prototype from late 2015. It does not
> run on current software and should not be deployed. It is kept for
> historical reference only.

monteio priced stock options with a Monte Carlo simulation, through three
pieces:

- `web/`: Django 1.8 site with signup/login, a pricing form, and a monthly
  per-user quota.
- `monteREST/`: Flask REST API built on Miguel Grinberg's
  [REST-auth](https://github.com/miguelgrinberg/REST-auth) example, whose
  README it still carries.
- `android_app/`: Android client for the REST API.

## Why it doesn't run

- It is written for Python 2.
- Its market-data sources, `pandas.io.data` and the Yahoo Finance CSV
  endpoints used by `ystockquote`, were shut down in 2016–2017.
- Django 1.8, Flask 0.10 and the Android toolchain (Gradle plugin 1.2,
  jcenter, support library 22) are long obsolete.

## Known problems

Do not reuse this code as is.

- **Security:** the Flask API runs the Werkzeug debugger on `0.0.0.0` (remote
  code execution), and secret keys are hardcoded in `monteREST/api.py` and
  `web/base/settings.py`. The Android app sends credentials over plain HTTP.
- **Pricing model:** the simulation averages Black-Scholes values along each
  path instead of pricing the payoff at expiry, annualises volatility with
  √365 rather than √252, and uses the Laplace scale as the Gaussian standard
  deviation. Prices come out roughly 20% too high.
- **Bugs:** `days=1` crashes, failed requests still use up quota, and the
  monthly quota resets wrongly after a year.
