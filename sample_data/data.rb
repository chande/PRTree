require 'factual'
factual = Factual.new("v7S19BHujTqZldU1trv2yx45MgzANmEh1vPx3T6b", "DQk6GL4ga47YqW1WrqQnE4c54tGKNcQVbAQ7ISWQ")

def printout(r)
  for x in r
    if x['longitude'] != nil
      puts x['longitude'].to_s + ',' + x['latitude'].to_s + ',"' + x['name'] + "\""
    end
  end
end

def getData(ar, factual, cat)
  c = "US"
  for x in ar
    first = factual.table("places").select(:name, :longitude, :latitude).filters({"country" => c, "region" => x, "category_ids" => cat}).limit(50)
    second = factual.table("places").select(:name, :longitude, :latitude).filters({"country" => c, "region" => x, "category_ids" => cat}).offset(50).limit(50)
    third = factual.table("places").select(:name, :longitude, :latitude).filters({"country" => c, "region" => x, "category_ids" => cat}).offset(100).limit(50)
    fourth = factual.table("places").select(:name, :longitude, :latitude).filters({"country" => c, "region" => x, "category_ids" => cat}).offset(150).limit(50)
    fifth = factual.table("places").select(:name, :longitude, :latitude).filters({"country" => c, "region" => x, "category_ids" => cat}).offset(200).limit(50)
    sixth = factual.table("places").select(:name, :longitude, :latitude).filters({"country" => c, "region" => x, "category_ids" => cat}).offset(250).limit(50)
   seventh = factual.table("places").select(:name, :longitude, :latitude).filters({"country" => c, "region" => x, "category_ids" => cat}).offset(300).limit(50)
    eighth = factual.table("places").select(:name, :longitude, :latitude).filters({"country" => c, "region" => x, "category_ids" => cat}).offset(350).limit(50)
    ninth = factual.table("places").select(:name, :longitude, :latitude).filters({"country" => c, "region" => x, "category_ids" => cat}).offset(400).limit(50)
    tenth = factual.table("places").select(:name, :longitude, :latitude).filters({"country" => c, "region" => x, "category_ids" => cat}).offset(450).limit(50)
    printout(first)
    printout(second)
    printout(third)
    printout(fourth)
    printout(fifth)
    printout(sixth)
    printout(seventh)
    printout(eighth)
    printout(ninth)
    printout(tenth)
  end
end

#puts factual.table("places").schema
category = "415"
state = "CA,TX,FL,NY,PA,IL,OH,MI,GA,NJ,NC,MA,VA,WA,TN,MN,WI,MO,MD,CO,IN,AZ,OR,AL,CT,LA,SC,KY,OK,IA,AR,KS,NV,MS,UT,NE,NM,NH,ID,ME,WV,DC,HI,RI,MT,DE,SD,ND,AK,VT"
statesArray = state.split(',')
getData(statesArray, factual, category)
