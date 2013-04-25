numberOfResults = 25
rootStr = "prt.root"

#range defines number of queries to perform during this run
for i in 0 .. 250
  str = (0...3).map{ ('a'..'z').to_a[rand(26)] }.join

  firstNum = (-(rand(99)))
  secondNum = rand(99)

  firstMantissa = rand()
  secondMantissa = rand()

  lat = firstNum+ firstMantissa
  lon = secondNum + firstMantissa

  query = str + "," + lat.to_s + "," + lon.to_s + "," + numberOfResults.to_s
  puts query
end



