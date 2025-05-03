var barChart = document.getElementById("barChartMonthly").getContext("2d");

var myBarChart = new Chart(barChart, {
  type: "bar",
  data: {
    labels: Array.from({ length: 31 }, (_, i) => `${i + 1}`), // Génère les labels Day 1 à Day 31
    datasets: [
      {
        label: "Sales (€)", // Modifiez "Sales" selon votre contexte
        backgroundColor: "rgba(23, 125, 255, 0.6)", // Couleur des barres (avec transparence)
        borderColor: "rgb(23, 125, 255)", // Couleur des bordures
        borderWidth: 1,
        data: [
          120, 140, 110, 150, 130, 170, 180, 190, 200, 220, 240, 250, 230, 210, 190, 180,
          160, 170, 150, 140, 130, 120, 110, 100, 90, 80, 70, 60, 50, 40, 30,
        ], // Exemple de données (remplacez avec les vôtres)
      },
    ],
  },
  options: {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      x: {
        title: {
          display: true,
          text: "Days of the Month", // Légende de l'axe X
        },
        ticks: {
          maxRotation: 0,
          minRotation: 0,
        },
      },
      y: {
        beginAtZero: true,
        title: {
          display: true,
          text: "Sales (€)", // Légende de l'axe Y
        },
        ticks: {
          stepSize: 50, // Ajustez le pas selon vos données
        },
      },
    },
    plugins: {
      legend: {
        display: true,
        position: "top",
      },
      tooltip: {
        callbacks: {
          label: function (context) {
            return `${context.dataset.label}: €${context.raw}`; // Formatte le tooltip avec €
          },
        },
      },
    },
  },
});
