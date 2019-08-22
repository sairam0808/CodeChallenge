const mysql = require('mysql');
const fs = require('fs');

const config = require('./config');

const connection = mysql.createConnection(config);

const getCreateQuery = (table, columns) => (
  `CREATE TABLE ${table} (${Object.entries(columns).map(column => column.join(' ')).join()})`
);

const getInsertQuery = (table, data) => (
  `INSERT INTO ${table} VALUES (${data})`
);

const data1 = getCreateQuery('data1', {
  created_at: 'DATETIME',
  first_name: 'VARCHAR(100)',
  last_name: 'VARCHAR(100)',
  email: 'VARCHAR(100)',
  latitude: 'FLOAT',
  longitude: 'FLOAT',
  ip: 'VARCHAR(100)',
});

const data2 = getCreateQuery('data2', {
  created_at: 'DATETIME',
  ip: 'VARCHAR(100)',
  latitude: 'FLOAT',
  longitude: 'FLOAT',
  first_name: 'VARCHAR(100)',
  last_name: 'VARCHAR(100)',
  email: 'VARCHAR(100)',
});

const datas = {
  data1: {
    schema: data1,
    indexes: [4, 5],
  },
  data2: {
    schema: data2,
    indexes: [2, 3],
  },
};

connection.connect();

Object.entries(datas).forEach((eachData) => {
  connection.beginTransaction(err => {
    if (err) throw err;
    connection.query(eachData[1].schema, (error) => {
      if (error) console.error(error);
      else {
        console.log(`CREATED ${eachData[0]}`);
        const data = fs.readFileSync(`${eachData[0]}.csv`).toString().split('\n');
        data.forEach((row, index) => {
          if (index === 0) {
            return;
          }
          const transformedRow = row.split(',').map((value, index) => {
            if (eachData[1].indexes.includes(index)) {
              return parseFloat(value) || 0;
            }
            return `'${value}'`;
          }).join();
          connection.query(getInsertQuery(eachData[0], transformedRow), (error) => {
            if (error) {
              console.error('Insert Error', error);
              return connection.rollback(() => { throw err; });
            }
            connection.commit((err) => {
              if (err) {
                return connection.rollback(() => { throw err; });
              }
              console.log('INSERTED');
            });
          });
        });
      }
    });
  });
});
