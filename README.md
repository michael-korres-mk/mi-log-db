# MiLogDB
#### _Michael Korres - Department of Informatics & Telecommunications - University of Athens_

## Data Import
Some numbers:

| Batch Size | Time (s) | Time (min) |
|------------|----------|-------------|
| 1          | 3296.01  | ~54.93      |
| 100        | 126.17   | ~2.10       |
| 200        | 112.40   | ~1.87       |
| 500        | 97.02    | ~1.62       |
| 1000       | 94.21    | ~1.57       |

- Persist per 10 batches in memory
- Stayed with Batch Size = 1000 w/ around 2min on average (parsing & persisting)


## Security
- Spring Security handles login (POST).
- UserController handles signup (POST).
- The "/" page is the combined login/signup UI.