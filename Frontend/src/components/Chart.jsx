import { useState, useMemo } from 'react';
import { Box, Heading } from '@chakra-ui/react';
import {
  PieChart,
  Pie,
  Cell,
  Legend,
  ResponsiveContainer
} from 'recharts';

const COLORS = {
  Positivo: '#4CAF50',
  Negativo: '#F44336',
  Neutro: '#9E9E9E',
  Inconclusivo: '#FF9800'
};

//  MOCK para testes locais

export default function Chart({data}) {
  const [activeKeys] = useState([
    'Positivo', 'Negativo', 'Neutro', 'Inconclusivo'
  ]);

  const processedData = useMemo(() => {
    const { count, positivesFeedbacks, negativeFeedbacks, neutralFeedbacks } = data;
    const inconclusives = count - positivesFeedbacks - negativeFeedbacks - neutralFeedbacks;

    const allData = [
      { name: 'Positivo', value: positivesFeedbacks },
      { name: 'Negativo', value: negativeFeedbacks },
      { name: 'Neutro', value: neutralFeedbacks },
      { name: 'Inconclusivo', value: inconclusives }
    ];

    return allData.filter(item => item.value > 0);
  }, [data]);

  const filteredData = processedData.filter(item => activeKeys.includes(item.name));
  const total = filteredData.reduce((sum, item) => sum + item.value, 0);

  return (
    <Box w="full">
      <Heading size="md" mb={4}>
        % por sentimentos (Total: {total})
      </Heading>
      <ResponsiveContainer width="100%" height={375}>
        <PieChart>
          <Pie
            data={filteredData}
            dataKey="value"
            nameKey="name"
            cx="50%"
            cy="50%"
            outerRadius={80}
            labelLine={false}
            label={({ cx, cy, midAngle, innerRadius, outerRadius, index }) => {
              const RADIAN = Math.PI / 180;
              const radius = innerRadius + (outerRadius - innerRadius) * 1.2;
              const x = cx + radius * Math.cos(-midAngle * RADIAN);
              const y = cy + radius * Math.sin(-midAngle * RADIAN);
              return (
                <text
                  x={x}
                  y={y}
                  fill={COLORS[filteredData[index].name]}
                  fontSize={14}
                  textAnchor={x > cx ? 'start' : 'end'}
                  dominantBaseline="central"
                >
                  {`${filteredData[index].name}: ${((filteredData[index].value / total) * 100).toFixed(1)}%`}
                </text>
              );
            }}
            isAnimationActive={true}
            animationDuration={500}
          >
            {filteredData.map((entry) => (
              <Cell key={`cell-${entry.name}`} fill={COLORS[entry.name]} />
            ))}
          </Pie>
          <Legend align="center" />
        </PieChart>
      </ResponsiveContainer>
    </Box>
  );
}