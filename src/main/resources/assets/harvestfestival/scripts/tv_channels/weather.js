/** Called when the tv channel is activated, we then use this and decide
 * which tv program should be played on the tv as well as what the tv chatters
 *
 * WEATHER CHANNEL:
 * The player will find out what the weather forecast is tomorrow
 * We will set the program to whatever the relevant weather condition is
 * We will also then display some random chatter variants for the weather
 *
 * @param {PlayerJS}      player      -   the player interacting with the tv
 * @param {TelevisionJS}  television  -   the television object**/
function watch (player, television) {
  var tomorrow = weather.tomorrow(player.world())
  var snows = player.world().biome(player.pos()).snows()
  var index = calendar.day(player.world()) % 3 + 1
  if (tomorrow == clear) {
    television.watch('harvestfestival:weather_clear')
    television.chatter(player, 'harvestfestival.television.channel.weather.forecast.clear.' + index)
  } else if (tomorrow == rain) {
    television.watch('harvestfestival:weather_' + (snows ? 'snow' : 'rain'))
    television.chatter(player, 'harvestfestival.television.channel.weather.forecast.' + (snows ? 'snow.' : 'rain.') + index)
  } else if (tomorrow == storm) {
    television.watch('harvestfestival:weather_' + (snows ? 'blizzard' : 'storm'))
    television.chatter(player, 'harvestfestival.television.channel.weather.forecast.' + (snows ? 'blizzard.' : 'storm.'))
  }
}
