# Walmart Labs Challenge - Drone Delivery Challenge

## Assumptions

1. Drone can only carry one delivery.
2. Drone can only fly horizontally or vertically, not any other direction.
3. Drone have to pick up the delivery in the warehouse before delivering them.
4. During the calculation for the best solution, drone can know the order before it is created.

## Build

The project is built within maven tool, so we can build the solution with maven command:

```
mvn clean install
```

## Commands

```
usage: DroneDelivery
 -all,--all            try all the controller
 -dfs,--dfs            Use brute force dfs controller
 -fcfs,--fcfs          Use first come first serve controller
 -g,--generate         Flag to generate data
 -h,--help             Print help information
 -i,--input <arg>      Input file path
 -n,--number <arg>     Set number of orders to generate
 -out,--output <arg>   output file path
 -r,--range <arg>      Set the range for generated coordinate
 -sjf,--sjf            Use shortest job first controller
```

### Load Data

Here we have two method to load the data: Random generate and load from file.

+ Random generated

  With `-g`,  we can let the program to random generate the input data. We have two arguments to control the generation. 

  `-n` can control the total number of orders to generate, defaultly be set to 15.

   `-r` can control the max range of the coordinate of the order, defaultly be set to 30.

+ Load from file

  With `-i` argument, we can assign a file path to load the input data.

### Three Controllers

Here I design three controllers for different situations and usages. With all these three controllers, we can use `-out` argument to define the output file path.

#### First come first serve

With this controller, the drone will directly choose the order which have the earliest start time to deliever. This selection is easy to made, overall the time complexity will be just O(n).

With `-fcfs` argument, we can use the first come first serve controller.

#### Shortest job first

With this controller, the drone will maintain a wait list for the orders, and everytime the drone will choose the order with shortest serve time to deliver.

With `-sjf` argument, we can use the shortest job first controller.

#### Brute force dfs

With this controller, we assume that the drone knows all the order at the begining, and it will try all the possible combination of deliver sequences, and find the best one with highest NPS value.

Note: this controller can be very time-consuming, especially for data with high numbers and low coordinate.

With `-dfs` argument, we can user the dfs controller.



